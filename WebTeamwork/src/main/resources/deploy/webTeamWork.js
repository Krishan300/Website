var content = (function () {
    function content() {
    }
    //requests data from server via an ajax call and passes it to show
    content.getAndShow = function () {
        $.ajax({
            method: "GET",
            url: "data.json",
            success: content.show
        });
    };
    content.show = function (data) {
        $("indexMain").html(Handlebars.templates['content.hb'](data));
    };
    return content;
}());
var nav = (function () {
    function nav() {
    }
    nav.init = function () {
        $("indexNav").html(Handlebars.templates['nav.hb']());
        $("#navContentBtn").click(nav.onContentClick);
        $("navWelcomeBtn").click(nav.onWelcomeClick);
    };
    nav.highlight = function (which) {
        $("indexNav li").removeClass("active");
        $(which).parent().addClass("active");
    };
    nav.onContentClick = function () {
        nav.highlight(this);
        content.getAndShow();
    };
    nav.onWelcomeClick = function () {
        nav.highlight(this);
        welcome.putInDom();
    };
    return nav;
}());
var welcome = (function () {
    function welcome() {
    }
    welcome.putInDom = function () {
        $("#indexMain").html(Handlebars.templates['welcome.hb']({
            when: new Date()
        }));
        $("indexMain").html(Handlebars.templates['loginpage.hb'])({});
    };
    return welcome;
}());
var login = (function () {
    function login() {
    }
    login.init = function () {
        $("indexLogin").html(Handlebars.templates['login.hb']);
        $("loginSubmitButton").click(login.sendUsername());
    };
    /**sends password and username to backend
     *
     */
    login.sendUsername = function () {
        //takes user name and password and sends it to backend via post route
        var usernameinput = $("username").val();
        var passwordinput = $("password").val();
        $.ajax({
            type: "POST",
            url: "/data/pw/",
            data: JSON.stringify({ usernameinput: usernameinput, passwordinput: passwordinput }),
            dataType: "json",
            success: function (data) {
                if (data.res == "ok") {
                    getJsonFromServer();
                }
                else {
                    window.alert("Have you forgot your password?");
                }
            }
        });
        function getJsonFromServer() {
            $.ajax({
                type: "GET",
                url: "/data/pw/vald",
                data: JSON,
                dataType: "json",
                success: function (data) {
                    if (data.res == "ok") {
                        //CommentBox.boxDisplay();
                    }
                    else {
                        window.alert("username or password is not valid");
                    }
                }
            });
        }
    };
    return login;
}());
/** Creates a comment input box, allows for title & comment
 *boxDisplay returns html code for displaying class on a web page
 *sendComment sends a post route to the database
 */
var $;
var Handlebars;
$(document).ready(function () {
    login.init();
    login.sendUsername();
});
//displays comments
/* Place to write and display comments */
var commentBox = (function () {
    function commentBox() {
    }
    /** Initializes a page with a textbook and input button */
    commentBox.init = function () {
        $("indexCommentBox").html(Handlebars.templates('commentBox.hb')());
        $("commentButton").click(commentBox.sendComment);
    };
    commentBox.boxDisplay = function () {
        return "<input id='newTitle' size='50' maxlength='50' type='text' value='A title for your post! 50 characters max.'>"
            + " </input>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>"
            + "<button id='sendComment' onclick='sendComment()'>Send Comment</button>";
    };
    /**
     * takes data from textboxes and converts it into a full json object, sent to the db via a POST route
     * shows an error if it can't send the data, otherwise reloads the page via getDataFromServer
     */
    commentBox.sendComment = function () {
        var title = document.getElementById("newTitle").textContent;
        var comment = document.getElementById("newComment").textContent;
        title = (title === "") ? null : title;
        comment = (comment === "") ? null : title;
        $.ajax({
            type: "POST",
            url: "/data",
            data: JSON.stringify({ "title": title,
                "comment": comment,
                "likes": 0, "uploadDate": Date.now(), "likeDate": Date.now() }),
            dataType: "json",
            success: function (data) {
                if (data.res === "ok") {
                    getDataFromServer();
                }
                else {
                    window.alert("Error sending message: try again later.");
                }
            }
        });
    };
    commentBox.highlight = function (which) {
        $("commentBox").removeClass("active");
        $("commentBox").removeClass("active");
    };
    //Handle a click of the post message button
    commentBox.send = function () {
        commentBox.sendComment();
    };
    return commentBox;
}());
