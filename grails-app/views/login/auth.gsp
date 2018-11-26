<!doctype html>
<html lang="fr">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="For POC purpose">
    <meta name="author" content="Cheikh Tidiane SECK">
    <asset:link rel="icon" href="favicon.ico"></asset:link>

    <title>Signin Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <asset:stylesheet src="bootstrap4.css"></asset:stylesheet>

    <!-- Custom styles for this template -->
    <asset:stylesheet src="signin.css"></asset:stylesheet>
  </head>

  <body class="text-center">
    <form class="form-signin" action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" autocomplete="off">
      <asset:image class="mb-4" src="bootstrap-solid.svg" alt="" width="72" height="72"></asset:image>
      <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
      <label for="username" class="sr-only">Username</label>
      <input type="text" name="${usernameParameter ?: 'username'}" id="username" class="form-control" required autofocus>
      <label for="password" class="sr-only">Password</label>
      <input type="password" name="${passwordParameter ?: 'password'}" id="password" class="form-control" placeholder="Password" required>
      <div class="checkbox mb-3">
        <label>
          <input type="checkbox" name="${rememberMeParameter ?: 'remember-me'}" <g:if test='${hasCookie}'>checked="checked"</g:if>> Remember me
        </label>
      </div>
      <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      <p class="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
    </form>
  <script>
      (function() {
          document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
      })();
  </script>
  </body>
</html>
