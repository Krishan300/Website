/**
 * Created by Robert Salay on 2/12/2017.
 */
/** Creates a comment input box, allows for title & comment
 *boxDisplay returns html code for displaying class on a web page
 *sendComment sends a post route to the database
 */
var CommentBox = (function () {
    function CommentBox() {
    }
<<<<<<<<< Temporary merge branch 1
    /**
     *
     * @returns {html to display object on web page}
     */
=========
    //display comment creation box
>>>>>>>>> Temporary merge branch 2
    CommentBox.prototype.boxDisplay = function () {
        return "<input id='newTitle' size='50' maxlength='50' type='text' value='A title for your post! 50 characters max.'>"
            + " </input>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>"
            + "<button id='sendComment' onclick='sendComment()'>Send Comment</button>";
    };
<<<<<<<<< Temporary merge branch 1
    /**
     * takes data from textboxes and converts it into a full json object, sent to the db via a POST route
     * shows an error if it can't send the data, otherwise reloads the page via getDataFromServer
     */
=========
    //sends custom comment and title to db, checks for null
>>>>>>>>> Temporary merge branch 2
    CommentBox.prototype.sendComment = function () {
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
    return CommentBox;
}());
/** Creates an individual comment in a comment list
*@param index number describing comment
*@param title title of comment
*@param comment text of comment
*@param likes number of likes/dislikes on comments
*@param uploadDate Date object indicating time uploaded
*@param likeDate Date object indicating last time voted upon
*
*commentDisplay returns html code in a string detailing how to display the object on a web page
*likeClick and dislikeClick both send a put route that calls for their comment's like and likeDate parameters to be altered
 */
var CommentList = (function () {
    /**
     *initializes the parameters for a CommentList object
     */
    function CommentList(index, title, comment, likes, uploadDate, likeDate) {
        this.index = index;
        this.title = title;
        this.comment = comment;
        this.likes = likes;
        this.uploadDate = uploadDate;
        this.likeDate = likeDate;
    }
<<<<<<<<< Temporary merge branch 1
    /**
     *
     * @returns {html code to display CommentList on a web page}
     */
=========
    //displays comment
>>>>>>>>> Temporary merge branch 2
    CommentList.prototype.commentDisplay = function () {
        return "<hr> <h1>" + this.title + "</h1>"
            + "<p>" + this.comment + "</p><br>"
            + "<button id='like' onclick='likeClick()'>Like</button>"
            + this.likes + "<button id='dislike' onclick='dislikeClick()'>Dislike</button><br>"
            + "<p>This comment was posted on </p>" + this.uploadDate
            + "<p> and was last liked on </p>" + this.likeDate + "<br>";
    };
<<<<<<<<< Temporary merge branch 1
    /**
     * updates a comment's like # and like date via a PUT route.
     * shows an error if it cannot do so, otherwise reloads the page via getDataFromServer
     */
=========
    //sends message to db about specific comment being liked
>>>>>>>>> Temporary merge branch 2
    CommentList.prototype.likeClick = function () {
        $.ajax({
            type: "PUT",
            url: "data/like/up/" + this.index.toString(),
            data: JSON.stringify({ "index": this.index, "title": this.title, "comment": this.comment, "uploadDate": this.uploadDate, "likeDate": Date.now() }),
            dataType: "json",
            success: function (data) {
                if (data.res === "ok") {
                    getDataFromServer();
                }
                else {
                    window.alert("Error liking comment: try again later.");
                }
            }
        });
    };
<<<<<<<<< Temporary merge branch 1
    /**
     * updates a comment's like # and like date via a PUT route.
     * shows an error if it cannot do so, otherwise reloads the page via getDataFromServer
     */
=========
    //sends message to db about specific comment being disliked
>>>>>>>>> Temporary merge branch 2
    CommentList.prototype.dislikeClick = function () {
        $.ajax({
            type: "PUT",
            url: "data/like/down/" + this.index.toString(),
            data: JSON.stringify({ "index": this.index, "title": this.title, "comment": this.comment, "uploadDate": this.uploadDate, "likeDate": Date.now() }),
            dataType: "json",
            success: function (data) {
                if (data.res === "ok") {
                    getDataFromServer();
                }
                else {
                    window.alert("Error disliking comment: try again later.");
                }
            }
        });
    };
    return CommentList;
}());
/**
*requests data from db as a json object, resets the html, and turns json data into an array of commentList objects and displays them
 */
function getDataFromServer() {
    //clear html
<<<<<<<<< Temporary merge branch 1
    document.getElementById("commentBox").innerHTML = "";
    document.getElementById("listing").innerHTML = "";
    var cBox = new CommentBox();
    document.getElementById("commentBox").innerHTML = cBox.boxDisplay(); //display comment box
    /**
     * uses GET route to obtain data from db as JSON.
     * if possible, turns data into an array of commentList objects and displays them in reverse chronological order
     * if not, sends an error
     */
=========
    document.getElementById("header").innerHTML = "";
    document.getElementById("commentBox").innerHTML = "";
    document.getElementById("listing").innerHTML = "";
    var a = new CommentBox();
    document.getElementById("commentBox").innerHTML = a.boxDisplay(); //display comment box
>>>>>>>>> Temporary merge branch 2
    $.ajax({
        type: "GET",
        url: "/data",
        dataType: "json",
        success: function (data) {
<<<<<<<<< Temporary merge branch 1
            if (data.res === "okay") {
                var cList = new CommentList[data.length];
                //turn db data into comments stored in reverse chronological order
=========
            var b = new CommentList[data.length];
            //turn db data into comments stored in reverse chronological order
            for (var i in data) {
>>>>>>>>> Temporary merge branch 2
                var counter = 1;
                for (var i in data) {
                    cList[data.length - counter] = new CommentList(data[i].index, data[i].title, data[i].comment, data[i].likes, data[i].uploadDate, data[i].likeDate);
                    counter++;
                }
                //display all comments
                for (var k = 0; k < cList.length; k++) {
                    document.getElementById("listing").innerHTML += cList[k].commentDisplay();
                }
            }
<<<<<<<<< Temporary merge branch 1
            else {
                window.alert("Cannot get web data at this time. Try again later.");
=========
            //display all comments
            for (var _i = 0, b_1 = b; _i < b_1.length; _i++) {
                var j = b_1[_i];
                document.getElementById("listing").innerHTML += j.commentDisplay();
>>>>>>>>> Temporary merge branch 2
            }
        }
    });
}
<<<<<<<<< Temporary merge branch 1
//calls getDataFromServer to initialize page
=========
document.getElementById("header").innerHTML = "<h1 align='center'>The Buzz:</h1>  <h2 align='center'>The newest social media out there!</h2> <hr>";
>>>>>>>>> Temporary merge branch 2
getDataFromServer();
