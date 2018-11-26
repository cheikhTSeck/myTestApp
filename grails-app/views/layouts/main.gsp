<g:set bean="springSecurityService" var="springSecurityService"></g:set>
<g:set value="${springSecurityService.currentUser?.username}" var="username"></g:set>
<!DOCTYPE html>
<html lang="fr">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>
        <g:layoutTitle></g:layoutTitle>
    </title>
    <!-- plugins:css -->
    <asset:stylesheet src="custom.css"></asset:stylesheet>
    <!-- endinject -->
    <asset:link rel="shortcut icon" href="avicon.png"></asset:link>
    <g:layoutHead></g:layoutHead>
</head>

<body>
<div class="container-scroller">
    <!-- partial:partials/_navbar.html -->
    <g:render template="/layouts/navbar"></g:render>
    <!-- partial -->
    <div class="container-fluid page-body-wrapper">
        <!-- partial:partials/_sidebar.html -->
        <g:render template="/layouts/sidebar"></g:render>
        <!-- partial -->
        <div class="main-panel" style="position: absolute; right: 0;">
            <div class="content-wrapper">
                <g:if test="${flash.message}">
                    <div class="alert alert-fill-success" role="alert">
                        <i class="mdi mdi-alert-circle"></i>
                        ${flash.message}
                    </div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="alert alert-fill-danger" role="alert">
                        <i class="mdi mdi-alert-circle"></i>
                        ${flash.error}
                    </div>
                </g:if>
                <g:layoutBody></g:layoutBody>
            </div>
            <!-- content-wrapper ends -->
            <!-- partial:partials/_footer.html -->
            <g:render template="/layouts/footer"></g:render>
            <!-- partial -->
        </div>
        <!-- main-panel ends -->
    </div>
    <!-- page-body-wrapper ends -->
</div>
<!-- container-scroller -->

<!-- plugins:js -->
<asset:javascript src="custom.js"></asset:javascript>
<!-- endinject -->
<!-- Custom js for this page-->
<asset:javascript src="dashboard.js"></asset:javascript>
<!-- End custom js for this page-->
</body>

</html>