/// Since this is a relatively simple demo, our welcome object
/// isn't going to do much. It has a method for putting a message
/// into the main content panel, and just for fun, we show that
/// the content is updated on refresh by displaying a date.
class welcome {
	/// putInDom will render the welcome.hb template, passing in
	/// the date:
	public static putInDom() {
		$("#indexMain").html("");
		$("#indexMain").html(Handlebars.templates['welcome.hb']({
			pagetitle: welcome.isLoggedIn(),
			pagecontent: welcome.displayLoginBox()
		}));
	}

	public static newUserLogin() {
		var myUser = $("#login-username").val();
        var myPass = $("#login-password").val();
        myUser = (myUser === "") ? null : myUser;
        myPass = (myPass === "") ? null : myPass;

        $.ajax({
            type: "POST",
            url: "/login",
            data: JSON.stringify({ username: myUser, password: myPass }),
            dataType: "json"
        }).done( function (data, status) {
        	///window.alert(data.secret_key);
        	///window.alert(data.username);
            var mySecretKey = data.secret_key;
            document.cookie = "user=" + myUser;
            document.cookie = "session=" + mySecretKey;
        }).fail( function (errortype, data, status) {
            window.alert("Login failed");
        });

        welcome.putInDom();
        nav.init();
	}

	public static isLoggedIn() {
		/// TODO: Do a less liberal sweep of cookies to see if the cookie is actually valid. Might require another route to check.
		if (document.cookie === "") {
			return "Log in";
		}
		else {
			return welcome.getCookieValue("user");
		}
	}

	public static displayLoginBox() {
		if (document.cookie === "") {
			return "<div id='loginBox'>Username: <input id='login-username' type='text'></input><br>Password: <input id='login-password' type='password' name='password'></input><br><button id='login-submit-btn' onclick='welcome.newUserLogin()'>Login</button></div>";
		}
		else {
			return "<p>You are logged in.</p><br><br><br><br><button id='logout-submit-btn' onclick='welcome.userLogout()'>Logout</button></div>";
		}
	}

	public static userLogout() {
		$.ajax({
            type: "GET",
            url: "/logout",
            success: welcome.putInDom
        });
        /// Delete the cookies.
        document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
        document.cookie = "session=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";

        welcome.putInDom();
        nav.init();
	}

	public static getCookieValue(a) {
	    var b = document.cookie.match('(^|;)\\s*' + a + '\\s*=\\s*([^;]+)');
	    return b ? b.pop() : '';
	}
}