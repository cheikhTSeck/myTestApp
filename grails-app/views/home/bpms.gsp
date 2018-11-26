<head>
    <!-- Required meta tags -->
    <meta name="layout" content="main">
    <title>Liste des workflows</title>
</head>

<body>
<div class="row">
    <div class="col-md-12 grid-margin stretch-card accordion basic-accordion">
        <div class="card">
            <div class="card-header">
                <h6 class="mb-0">
                    <a data-toggle="collapse" href="#collapseOne" aria-expanded="false" aria-controls="collapseOne"
                       class="collapsed">
                        <i class="card-icon mdi mdi-checkbox-marked-circle-outline"></i>
                        Nouveau déploiement
                    </a>
                </h6>
            </div>

            <div id="collapseOne" class="collapse" role="tabpanel" aria-labelledby="headingOne" data-parent="#accordion"
                 style="">
                <div class="card-body">
                    <g:form action="deploiement" enctype="multipart/form-data" class="forms-sample">
                        <div class="form-group">
                            <label>Fichier</label>
                            <input type="file" name="bpmn" class="file-upload-default">

                            <div class="input-group col-xs-12">
                                <input type="text" class="form-control file-upload-info" disabled
                                       placeholder="Upload Image" name="processe" accept=".bpmn">
                                <span class="input-group-append">
                                    <button class="file-upload-browse btn btn-info" accept=".bpmn"
                                            type="button">Charger</button>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="2"></textarea>
                        </div>
                        <input type="submit" class="btn btn-success mr-2" value="Enregistrer"></input>
                        <button class="btn btn-light">Cancel</button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <div class="row justify-content-between">
                    <span class="col-md-3">
                        <h6 class="mb-3">
                            Liste des workflows
                        </h6>
                    </span>
                    <span class="col-md-2">
                        <g:link class="btn btn-danger" action="rase">Réinitialiser</g:link>
                    </span>
                </div>

                <div class="row">
                    <div class="table-responsive">
                        <table class="table table-hover table-striped">
                            <thead>
                            <tr>
                                <th>Process</th>
                                <th>Description</th>
                                <th>Version</th>
                                <th>Date</th>
                                <th>BPMN</th>
                                <th>Apercu</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${processes}" var="process">
                                <g:set var="workflowService" bean="workflowService"></g:set>
                                <g:set var="deploiement"
                                       value="${workflowService.getDeploiement(process.deploymentId)}"></g:set>
                                <tr>
                                    <td>${process.name}</td>
                                    <td>${process.description}</td>
                                    <td>${process.versionTag}</td>
                                    <td>${deploiement?.deploymentTime?.format("dd/MM/yyyy à HH:mm")}</td>
                                    <td>
                                        <g:link action="downloadProcess"
                                                params="[processDefId: process.id, processName: process.name]">
                                            <i class="icon-cloud-download"></i>
                                            <g:message code="default.download.label"
                                                       default="Télécharger"></g:message>
                                        </g:link>
                                    </td>
                                    <td>
                                        %{--<g:link action="downloadApercu"
                                                params="[processDefId: process.id, processName: process.name]">
                                            <i class="icon-cloud-download"></i>
                                            <g:message code="default.download.label"
                                                       default="Télécharger"></g:message>
                                        </g:link>--}%
                                        <a href="javascript:;" onclick='showWorkflow("${process.id}");'>
                                            <i class="icon-eye"></i>
                                            <g:message code="default.visualize.label"
                                                       default="Visualiser"></g:message>
                                        </a>
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
</div>
</body>
