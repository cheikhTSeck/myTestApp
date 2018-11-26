<%@ page import="poc.aldiouma.User" %>
<head>
    <meta name="layout" content="main">
    <title>Démarrage processus</title>
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

                <g:form class="forms-sample" action="saveProject">
                    <g:hiddenField name="taskId" value="${taskId}"></g:hiddenField>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group row">
                                <label for="intitule">Intitulé</label>
                                <input type="text" class="form-control" name="intitule" id="intitule" placeholder="Name"
                                       required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="validateur">Destinataire(facultatif)</label>
                                <g:select class="form-control" name="validateur" from="${validateurs}"
                                          optionKey="username" optionValue="username"></g:select>
                            </div>
                        </div>
                    </div>


                    <div class="form-group row">
                        <label for="desc">Textarea</label>
                        <textarea class="form-control" id="desc" name="description" required rows="2"></textarea>
                    </div>
                    <button type="submit" class="btn btn-success mr-2">Submit</button>
                    <button class="btn btn-light">Cancel</button>
                </g:form>
            </div>
        </div>
    </div>
</div>
</body>

</html>