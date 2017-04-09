/// The content object is responsible for filling the 'indexMain'
/// div with the result of an AJAX query to get the most recent data.
var content = (function () {
    function content() {
    }
    /// getAndShow will request some data from the server via AJAX call.
    /// When data arrives, it will be passed to show()
    ///
    /// NB: remember that every file request is a GET, so we can
    /// put 'data.json' in the file hierarchy, and then GET it as
    /// a way of mocking a REST route.
    ///
    /// I cannot do this until the backend is up and running. The backend will provide
    /// some route I can use to get a certain article in realtime. Until then, use dummy text.
    /// I have created a version of the backend (based on a round of backend code finished during
    /// our two days of catch-up after phase 1) that I will base these actions on. I didn't want to 
    /// even vaguely appear to be cheating, so this version of the backend won't be uploaded until after the due date
    /// has passed by at least a day. Our backend developer for phase 3 didn't even get the backend converted
    /// to POSTGRES, which should've been done during phase 2 but wasn't, so I had to do this in order to test
    /// anything in my frontend. All in all, it's been a really long uphill battle, but I want to make it clear
    /// that under no circumstances did I share the backend code with ANYONE in the group, especially our backend
    /// developer, and that I did it not in the interest of phase 3 as a whole but in the interest of having something
    /// to test my frontend with. My apologies if this isn't allowed, I just didn't want to submit a long apology about
    /// why I couldn't do anything this week and I figured I'd at least try to build the routes phase 2 should've had,
    /// even if we're far from using OAuth on the server level. (This is of course with the added difficulty that nothing functional
    /// was finished from frontend during phase 2, so I was starting from phase 1 frontend code that I had written a long
    /// time ago as well. Yes, it's surely been an adventure.)
    ///
    content.getAndShow = function () {
        ///document.getElementById("#indexMain").innerHTML = "";
        $.ajax({
            method: "GET",
            url: "/data",
            dataType: "json",
            success: content.show
        });
    };
    /// When the data arrives from the server, render the content.hb
    /// template using the data.
    content.show = function (data) {
        if (data.res === "bad data") {
            $("#indexMain").html("Please log in to view content!");
        }
        else {
            $("#indexMain").html(Handlebars.templates['content.hb'](data));
        }
    };
    content.sendComment = function () {
        var myTitle = $("#comment-title").val();
        var myBody = $("#comment-body").val();
        myTitle = (myTitle === "") ? null : myTitle;
        myBody = (myBody === "") ? null : myBody;
        $.ajax({
            type: "POST",
            url: "/data",
            data: JSON.stringify({ user_id: 1, title: myTitle, body: myBody }),
            dataType: "json"
        }).done(function (data, status) {
            if (data.res === "ok") {
                this.getAndShow();
            }
            else {
                window.alert("Invalid input provided (title and comment cannot be empty)");
            }
        }).fail(function (errortype, data, status) {
            window.alert("Send comment failed");
        });
    };
    return content;
}());
/// The navigation bar has two buttons, one for the 'Welcome' page and one for the 'Content' page
var nav = (function () {
    function nav() {
    }
    /// Put the navbar onto the page and configure its buttons
    nav.init = function () {
        $("#indexNav").html(Handlebars.templates['nav.hb']({
            account: nav.loggedIn()
        }));
        $("#navContentBtn").click(nav.onContentClick);
        $("#navWelcomeBtn").click(nav.onWelcomeClick);
    };
    /// Manage the highlighting of whichever button corresponds
    /// to the active content on the page
    nav.highlight = function (which) {
        $("#indexNav li").removeClass("active");
        $(which).parent().addClass("active");
    };
    /// Handle a click of the "Content" button
    nav.onContentClick = function () {
        // NB: 'this' is whatever DOM element was clicked
        nav.highlight(this);
        content.getAndShow();
    };
    /// Handle a click of the "Welcome" button
    nav.onWelcomeClick = function () {
        nav.highlight(this);
        welcome.putInDom();
    };
    nav.loggedIn = function () {
        if (document.cookie === "") {
            return "Log in";
        }
        else {
            return welcome.getCookieValue("user");
        }
    };
    return nav;
}());
/// Since this is a relatively simple demo, our welcome object
/// isn't going to do much. It has a method for putting a message
/// into the main content panel, and just for fun, we show that
/// the content is updated on refresh by displaying a date.
var welcome = (function () {
    function welcome() {
    }
    /// putInDom will render the welcome.hb template, passing in
    /// the date:
    welcome.putInDom = function () {
        $("#indexMain").html("");
        $("#indexMain").html(Handlebars.templates['welcome.hb']({
            pagetitle: welcome.isLoggedIn(),
            pagecontent: welcome.displayLoginBox()
        }));
    };
    welcome.newUserLogin = function () {
        var myUser = $("#login-username").val();
        var myPass = $("#login-password").val();
        myUser = (myUser === "") ? null : myUser;
        myPass = (myPass === "") ? null : myPass;
        $.ajax({
            type: "POST",
            url: "/login",
            data: JSON.stringify({ username: myUser, password: myPass }),
            dataType: "json"
        }).done(function (data, status) {
            ///window.alert(data.secret_key);
            ///window.alert(data.username);
            var mySecretKey = data.secret_key;
            document.cookie = "user=" + myUser;
            document.cookie = "session=" + mySecretKey;
        }).fail(function (errortype, data, status) {
            window.alert("Login failed");
        });
        welcome.putInDom();
        nav.init();
    };
    welcome.isLoggedIn = function () {
        /// TODO: Do a less liberal sweep of cookies to see if the cookie is actually valid. Might require another route to check.
        if (document.cookie === "") {
            return "Log in";
        }
        else {
            return welcome.getCookieValue("user");
        }
    };
    welcome.displayLoginBox = function () {
        if (document.cookie === "") {
            return "<div id='loginBox'>Username: <input id='login-username' type='text'></input><br>Password: <input id='login-password' type='password' name='password'></input><br><button id='login-submit-btn' onclick='welcome.newUserLogin()'>Login</button></div>";
        }
        else {
            return "<p>You are logged in.</p><br><br><br><br><button id='logout-submit-btn' onclick='welcome.userLogout()'>Logout</button></div>";
        }
    };
    welcome.userLogout = function () {
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
    };
    welcome.getCookieValue = function (a) {
        var b = document.cookie.match('(^|;)\\s*' + a + '\\s*=\\s*([^;]+)');
        return b ? b.pop() : '';
    };
    return welcome;
}());
/// If we say that '$' and 'Handlebars' are both variables of type
/// any, we lose all static checking but we don't get any error
/// messages while compiling.
///
/// In general, this is a bad thing to do because it means that it
/// is on us to make sure we are using jQuery and Handlebars
/// correctly. For this tutorial, we'll let it slide...
var $;
var Handlebars;
/// This is equivalent to 'public static main()' in Java. It runs
/// once all of the files that comprise our program have loaded.
/// In this demo, all it does is initialize the navbar and simulate
/// a click on the Welcome button.
$(document).ready(function () {
    nav.init();
    nav.onWelcomeClick();
});
/// The content object is responsible for filling the 'indexMain'
/// div with the result of an AJAX query to get the most recent data.
var article = (function () {
    function article() {
    }
    /// getAndShow will request some data from the server via AJAX call.
    /// When data arrives, it will be passed to show()
    /// NOTE: This is for one article in particular, not for the whole list. CONTENT is the
    /// area where we get the full listing of messages.
    ///
    article.getAndShow = function (idx) {
        $.ajax({
            method: "GET",
            url: "/data/message/" + idx,
            dataType: "json",
            success: article.show
        });
    };
    /// When the data arrives from the server, render the content.hb
    /// template using the data.
    /// NOTE: Using dummy text here. Will replace with values received from backend once it works at all.
    article.show = function (data) {
        $("#indexMain").html(Handlebars.templates['article.hb'](data));
    };
    /// Upvote and downvote methods: will be updated once the backend
    /// database is properly created and verifiable in some form.
    article.upvote = function (idx) {
        ///window.alert("You have upvoted an item at index " + x);
        $.ajax({
            method: "POST",
            url: "/data/vote/up",
            /// NOTE: For now, since we have no login functionality, I'm simulating every vote as if done by user at id 1.
            data: JSON.stringify({ user_id: 1, message_id: idx }),
            dataType: "json",
            success: article.getAndShow(idx)
        });
    };
    article.downvote = function (idx) {
        ///window.alert("You have downvoted an item at index " + x);
        $.ajax({
            method: "POST",
            url: "/data/vote/down",
            /// NOTE: For now, since we have no login functionality, I'm simulating every vote as if done by user at id 1.
            data: JSON.stringify({ user_id: 1, message_id: idx }),
            dataType: "json",
            success: article.getAndShow(idx)
        });
    };
    return article;
}());
/// The content object is responsible for filling the 'indexMain'
/// div with the result of an AJAX query to get the most recent data.
var profile = (function () {
    function profile() {
    }
    /// getAndShow will request some data from the server via AJAX call.
    /// When data arrives, it will be passed to show()
    /// NOTE: This is for one profile page.
    ///
    profile.getAndShow = function (idx) {
        $.ajax({
            method: "GET",
            url: "/user/" + idx,
            dataType: "json",
            success: profile.show
        });
    };
    /// When the data arrives from the server, render the content.hb
    /// template using the data.
    /// NOTE: Using dummy text here. Will replace with values received from backend once it works at all.
    profile.show = function (data) {
        $("#indexMain").html(Handlebars.templates['profile.hb'](data));
    };
    return profile;
}());
