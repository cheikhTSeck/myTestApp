<head>
    <meta name="layout" content="main">
    <title>Validation processus</title>
    <!-- plugin css for this page -->
    <asset:stylesheet src="icheck.css"></asset:stylesheet>
    <!-- End plugin css for this page -->
</head>

<body>
<div class="row">

    <div class="col-md-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Basic form</h4>

                <form class="forms-sample">
                    <div class="form-group">
                        <label for="intitule">Intitulé</label>
                        <input type="text" class="form-control" id="intitule" value="${projet.intitule}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="desc">Textarea</label>
                        <textarea class="form-control" id="desc" rows="2"
                                  readonly>${projet.description}</textarea>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="col-md-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Décision</h4>
                <g:form class="forms-sample" action="validationProject">
                    <g:hiddenField name="taskId" value="${taskId}"></g:hiddenField>
                    <div class="form-group col-md-4">
                        <g:select class="form-control" name="decision" from="${decisions}" optionKey="key" optionValue="value"></g:select>
                    </div>
                    <button type="submit" class="btn btn-success mr-2">Enregistrer</button>
                    <g:link action="projets" class="btn btn-light">Retour</g:link>
                </g:form>
            </div>
        </div>
    </div>

</div>
</body>

</html>