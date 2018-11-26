package poc.aldiouma

import grails.transaction.Transactional
import org.camunda.bpm.engine.AuthorizationException
import org.camunda.bpm.engine.BadUserRequestException
import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.impl.javax.el.PropertyNotFoundException
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.task.Task

import javax.persistence.OptimisticLockException

class HomeController {

    def springSecurityService
    def repositoryService
    def runtimeService
    def taskService
    def historyService
    def workflowService

    static final String STATUT_EN_COURS_DE_TRAITEMENT = "ET"
    static final String STATUT_REJETE = "RJ"
    static final String STATUT_VALIDE = "VA"

    def index() {}

    def rase() {
        repositoryService.createDeploymentQuery().list().each {
            repositoryService.deleteDeployment(it.id, true)
        }
        User.list().each { utilisateur ->
            taskService.createTaskQuery().taskAssignee(utilisateur.username).list().each { tache ->
                deleteTask(tache.processInstanceId, tache.id, "Rase")
            }
        }
        taskService.createTaskQuery().taskUnnassigned().list().each { tache ->
            deleteTask(tache.processInstanceId, tache.id, "Rase")
        }

        Projet.executeUpdate("DELETE FROM Projet")
        flash.message = "La base de Camunda a été nettoyée!"
        redirect action: "bpms"
    }

    private void deleteTask(String processInstanceId, String taskId, String desc) {
        try {
            runtimeService.deleteProcessInstance(processInstanceId, desc)
        }
        catch (BadUserRequestException burex) {
            throw new BadUserRequestException("Erreur survenue lors de la suppression des tâches")
        }
        catch (org.camunda.bpm.engine.OptimisticLockingException oLex) {
            throw new OptimisticLockException("Erreur survenue lors de la suppression des tâches")
        }
        //Supprime la tâche ainsi que l'historique
        taskService.deleteTask(taskId, true)
    }

    private ServiceResponse hasExistingTask(String username) {

        ServiceResponse serviceResponse = new ServiceResponse()
        def taskList = taskService.createTaskQuery().taskAssignee(username).list()
        if (!taskList.empty) {
            taskList.any { task ->
                String valeur = historyService.createHistoricVariableInstanceQuery().taskIdIn(task.id).variableName("projetId").singleResult()?.value
                if (!valeur) {
                    serviceResponse.serviceOk = true
                    serviceResponse.objetInstance = task.id
                    return true
                }
            }
            if (!serviceResponse.serviceOk)
                serviceResponse.serviceOk = false
        } else {
            serviceResponse.serviceOk = false
        }

        return serviceResponse

    }

    def newProject() {

        User currentUser = (User) springSecurityService.currentUser
        String taskId
        def serviceResp = hasExistingTask(currentUser.username)
        if (!serviceResp.serviceOk) {
            def serviceResponse = startProcess(currentUser.username)
            if (!serviceResponse.serviceOk) {
                flash.error = serviceResponse.message
            }
            taskId = (String) serviceResponse.objetInstance
        } else taskId = (String) serviceResp.objetInstance

        List<User> validateurs = User.findAllByIdNotEqual(currentUser.id)

        render view: "basic_elements", model: [taskId: taskId, validateurs: validateurs]
    }

    def saveProject() {
        Projet.withTransaction {
            if (params.intitule == null || "".equals(params.intitule.trim())) {
                throw new IllegalArgumentException("L'intitulé du projet est obligatoire")
            }
            if (params.description == null || "".equals(params.description.trim())) {
                throw new IllegalArgumentException("La description du projet est obligatoire")
            }
            if (params.taskId == null || "".equals(params.taskId.trim())) {
                throw new IllegalArgumentException("L'id de la tâche est obligatoire")
            }

            def tache = taskService.createTaskQuery().taskId(params.taskId).singleResult()
            if (tache == null) {
                throw new IllegalArgumentException("La tâche n'éxiste pas")
            }
            User currentUser = (User) springSecurityService.currentUser
            String executionId = tache.executionId

            def projet = new Projet()
            projet.intitule = params.intitule
            projet.description = params.description
            projet.demandeur = currentUser
            projet.statut = STATUT_EN_COURS_DE_TRAITEMENT
            projet.initieLe = new Date()

            if (!projet.save()) {
                throw new IllegalArgumentException(projet.errors.toString())
            }

            def map = [:]
            map.put("projetId", projet.id)
            if (params.validateur && !"".equals(params.validateur.trim()) && !currentUser.username.equals(params.validateur) && exists(params.validateur)) {
                def varLocal = [:]
                varLocal.put("nextValidator", params.validateur)
                taskService.setVariablesLocal(params.taskId, varLocal)
            }
            runtimeService.setVariables(executionId, map)
            taskService.complete(tache.id)
            flash.message = "Votre demande vient d'être soumis à validation."
            redirect action: "projets"
        }
    }

    boolean exists(String username) {
        return User.findByUsername(username)!=null
    }

    ServiceResponse startProcess(String initiator) {

        ServiceResponse serviceResponse = new ServiceResponse()
        try {

            if (initiator == null || "".equals(initiator)) {
                throw new IllegalArgumentException("L'initiateur de la tâche n'a pas été défini")
            }
            def map = [:]

            String sessionUsernameKey = "SYSTEM"
            String processDefKey = "poc_Dioums"

            map.put("initiator", initiator)
            map.put("sessionUsernameKey", sessionUsernameKey)
            map.put("processDefKey", processDefKey)

            ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey, map)

            if (pi == null) {
                throw new IllegalArgumentException("Erreur(s) survenue(s) lors du démarrage du traitement du forçage")
            }

            def task = taskService.createTaskQuery().processInstanceId(pi.processInstanceId).taskAssignee(initiator).singleResult()
            serviceResponse.serviceOk = true
            serviceResponse.objetInstance = task.id

        }
        catch (ProcessEngineException ex) {
            serviceResponse.serviceOk = true
            serviceResponse.message = ex.message
        }
        catch (AuthorizationException ex) {
            serviceResponse.serviceOk = true
            serviceResponse.message = ex.message
        }
        catch (IllegalArgumentException ex) {
            serviceResponse.serviceOk = true
            serviceResponse.message = ex.message
        }

        return serviceResponse

    }

    def projets() {
        def projets = Projet.all
        [projets: projets]
    }

    def validation() {
        def projet = Projet.get(params.id)
        User currentUser = (User) springSecurityService.currentUser
        List<Task> runningTasks = workflowService.getRunningTasksForProject(projet.id, currentUser.username)
        if (runningTasks.empty) {
            flash.error = "Vous n'avez aucune tâche en attente de traitement pour ce projet"
            redirect action: "projets"
            return
        }
        def decisions = [
                "VA": "Valider",
                "RJ": "Rejeter"
        ]
        [projet: projet, decisions: decisions, taskId: runningTasks.first().id]
    }

    def validationProject() {

        try {
            if (params.taskId == null || "".equals(params.taskId)) {
                throw new IllegalArgumentException("Tâche inconnue!")
            }
            if (params.decision == null || "".equals(params.decision)) {
                throw new IllegalArgumentException("Veuillez choisir une décision!")
            }
            if (!Projet.constrainedProperties.statut.inList.contains(params.decision)) {
                throw new IllegalArgumentException("Votre décision n'est pas prise en compte par l'application!")
            }
            String decision = params.decision
            User currentUser = springSecurityService.currentUser

            Projet projet = workflowService.getProjectFromTask(params.taskId)
            def map = [:]
            map.put("decision", decision)
            map.put("validator", currentUser.username)
            taskService.setVariablesLocal(params.taskId, map)
            taskService.complete(params.taskId)

            flash.message = "Votre décision a été enregistrée, le processus va suivre son cours..."

            boolean isProcessFinished = workflowService.isProcessFinished(params.taskId)
            if (isProcessFinished) {
                projet.statut = decision
                projet.validateur = currentUser
                projet.valideLe = new Date()
                if (projet.save(flush: true) == null) {
                    throw new IllegalArgumentException(projet.errors.toString())
                }
                projet.refresh()
                flash.message = "Votre décision a été enregistrée, le processus est terminé."
            }
        }
        catch (IllegalArgumentException ex) {
            flash.error = ex.message
        }
        catch (PropertyNotFoundException ex) {
            log.error "PropertyNotFoundException: $ex.message"
            flash.error = "Une variable n'a pas pu être résolue. Consultez les logs pour plus de détails"
            redirect action: "validation"
            return
        }
        catch (ProcessEngineException ex) {
            log.error "ProcessEngineException: $ex.message"
            try {
                String substr = ex.message.substring(ex.localizedMessage.indexOf("identifier '"))
                String unresolvedVar = substr.substring(12, substr.indexOf("'", 13))
                flash.error = "La variable '$unresolvedVar' n'a pas pu être résolue"
            }
            catch (Exception e) {
                flash.error = e.message
            }

            redirect action: "validation"
            return
        }
        redirect action: "projets"
    }

    def bpms() {
        def processDef = repositoryService.createProcessDefinitionQuery().latestVersion().list()
        [processes: processDef]
    }

    @Transactional
    def deploiement() {
        try {
            def file = request.getFile("bpmn")
            String nomFichier = file?.getOriginalFilename()
            if (file == null || file.size == 0) {
                throw new IllegalArgumentException("Fichier manquant ou corrompu")
            }

            org.camunda.bpm.engine.repository.Deployment deploiement = repositoryService.createDeployment().addInputStream(nomFichier, file.inputStream).deploy()
            if (deploiement != null) {
                log.info("Process déployé")
                flash.message = "Process déployé"
            } else {
                log.error("Une erreur est survenue lors de la tentative de déploiement")
                flash.error = "Une erreur est survenue lors de la tentative de déploiement"
            }

        }
        catch (Exception ex) {
            log.error("Erreurs >>> ${ex.getMessage()}")
            flash.error = ex.message
        }
        redirect(action: "bpms")
    }

}
