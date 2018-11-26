package poc.aldiouma

import grails.gorm.transactions.Transactional
import org.camunda.bpm.engine.history.HistoricProcessInstance
import org.camunda.bpm.engine.history.HistoricTaskInstance
import org.camunda.bpm.engine.history.HistoricVariableInstance
import org.camunda.bpm.engine.task.Task

@Transactional
class WorkflowService {

    def repositoryService
    def taskService
    def historyService

    def getDeploiement(String deploymentId) {
        repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult()
    }

    def getHierarchicalSuperior(String username, Long projectId) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().variableValueEquals("projetId", projectId).singleResult()
        HistoricTaskInstance lastTask = historyService.createHistoricTaskInstanceQuery().processInstanceId(historicVariableInstance.processInstanceId).orderByHistoricTaskInstanceEndTime().desc().list().first()
        String nextValidator = historyService.createHistoricVariableInstanceQuery().taskIdIn(lastTask.id).variableName("nextValidator").singleResult()?.value
        return nextValidator ?: "directeur"
    }

    /**
     * Retourne la liste des tâches en cours d'exécution pour un projet donné et optionnellement assignées à un utilisateur
     * @param projectId
     * @param assignee
     * @return La liste des tâches en cours d'exécution pour un projet donné et optionnellement assignées à un utilisateur
     */
    List<Task> getRunningTasksForProject(Long projectId, String assignee = null) {
        def variableList = historyService.createHistoricVariableInstanceQuery().variableValueEquals("projetId", projectId).list()
        if (variableList.isEmpty())
            return []
        def processInstanceId = variableList.first().processInstanceId
        def runningTasks
        if (assignee)
            runningTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(assignee).list()
        else
            runningTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list()
        return runningTasks
    }

    /**
     * Retourne le projet auquel la tâche est liée
     * @param taskId
     * @return
     */
    Projet getProjectFromTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult()
        HistoricVariableInstance variable = historyService.createHistoricVariableInstanceQuery().processInstanceId(task.processInstanceId).variableName("projetId").singleResult()
        return Projet.get(variable.value)
    }

    /**
     *
     * @param taskId
     * @return true if the process attached to the task is finished and false otherwise
     */
    boolean isProcessFinished(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult()
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.processInstanceId).singleResult()
        return historicProcessInstance.endTime != null
    }

    /**
     *
     * @param projetId
     * @return
     */
    String getNiveau(Long projetId) {
        def niveaux = getRunningTasksForProject(projetId).assignee
        return niveaux.join(", ")
    }

}
