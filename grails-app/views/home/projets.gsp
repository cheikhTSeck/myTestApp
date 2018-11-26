<g:set var="workflowService" bean="workflowService"></g:set>
<g:set var="springSecurityService" bean="springSecurityService"></g:set>
<g:set value="${springSecurityService.currentUser?.username}" var="username"></g:set>
<head>
    <!-- Required meta tags -->
    <meta name="layout" content="main">
    <title>Liste des projets</title>
</head>

<body>
    <div class="row">
        <div class="col-lg-12 grid-margin stretch-card">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">Liste des projets</h4>

                    <div class="table-responsive">
                        <table class="table table-hover table-striped">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Titre</th>
                                    <th>Soumis par</th>
                                    <th>Niveau</th>
                                    <th>Statut</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${projets}" var="projet">
                                    <g:set var="niveau" value="${workflowService.getNiveau(projet.id)}"></g:set>
                                <tr>
                                    <td><g:formatDate date="${projet.initieLe}" format="dd/MM/yyyy à HH:mm:ss"></g:formatDate> </td>
                                    <td>
                                        <g:if test="${username.equals(niveau)}">
                                            <g:link action="validation" id="${projet.id}">
                                                ${projet.intitule}
                                            </g:link>
                                        </g:if>
                                        <g:else>
                                            ${projet.intitule}
                                        </g:else>
                                    </td>
                                    <td>${projet.demandeur.username}</td>
                                    <td>${niveau}</td>
                                    <td>
                                        <label class="badge badge-${message(code:'projet.statut.'+projet.statut+'.classe')}">
                                            <g:message code="projet.statut.${projet.statut}.label"></g:message>
                                        </label>
                                    </td>
                                </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
