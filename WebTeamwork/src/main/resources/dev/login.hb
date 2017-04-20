<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Login</h3>
	</div>
	<div class="panel-body">
		<input id='login-username' cols='50' type='text' placeholder="Username"></input><br>
		<input id='login-password' cols='50' type='text' placeholder="Password"></input><br>
		<button id="comment-send-btn" onclick="login.loginUser($("#login-username").val(), $("#login-password").val())">Login</button>
	</div>
</div>