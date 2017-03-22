/**
 * Created by Robert Salay on 2/12/2017.
 */
/** Creates a comment input box, allows for title & comment
 *boxDisplay returns html code for displaying class on a web page
 *sendComment sends a post route to the database
 */
var $;
var Handlebars;
var CommentBox = (function () {
    function CommentBox() {
    }
    /**
     * takes data from textboxes and converts it into a full json object, sent to the db via a POST route
     * shows an error if it can't send the data, otherwise reloads the page via getDataFromServer
     */
    CommentBox.prototype.sendComment = function () {
        var myTitle = $("#newTitle").val();
        var myComment = $("#newComment").val();
        myTitle = (myTitle === "") ? null : myTitle;
        myComment = (myComment === "") ? null : myComment;
        //var myDate = Date.now();
        // window.alert(myTitle);
        $.ajax({
            type: "POST",
            url: "/data",
            data: JSON.stringify({ title: myTitle, comment: myComment, numLikes: 0 }),
            dataType: "json"
        }).done( function (data, status) {
            if (data.res === "ok") {
                getDataFromServer();
            }
            else {
                window.alert("Error sending message: try again later.");
            }
        }).fail( function (errortype, data, status) {
            window.alert("CommentBox.sendComment: FAIL CALLED");
            window.alert(errortype.responseText);       // 500 Internal Error
            window.alert(errortype.statusText);         // Server Error
            window.alert(data.toString());              // error
            window.alert(status);                       // Server Error
        });
    };
    /**
     *
     * @returns {html to display object on web page}
     */
    CommentBox.prototype.boxDisplay = function () {
        return "<input id='newTitle' size='50' maxlength='50' type='text' value='A title for your post! 50 characters max.'>"
            + " </input><br>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>"
            + "<button type=button onclick='CommentBox.prototype.sendComment()'>Send Comment</button>";
    };
    return CommentBox;
}());
/** Creates an individual comment in a comment list
 *@param index number describing comment
 *@param title title of comment
 *@param comment text of comment
 *@param likes number of likes/dislikes on comments
 *@param uploadDate Date object indicating time uploaded
 *@param lastLikeDate Date object indicating last time voted upon
 *
 *commentDisplay returns html code in a string detailing how to display the object on a web page
 *likeClick and dislikeClick both send a put route that calls for their comment's like and lastLikeDate parameters to be altered
 */
var CommentList = (function () {
    /**
     *initializes the parameters for a CommentList object
     */
    function CommentList(index, title, comment, likes, uploadDate, lastLikeDate) {
        this.index = index;
        this.title = title;
        this.comment = comment;
        this.likes = likes;
        this.uploadDate = uploadDate;
        this.lastLikeDate = lastLikeDate;
    }
    /**
     * updates a comment's like # and like date via a PUT route.
     * shows an error if it cannot do so, otherwise reloads the page via getDataFromServer
     */
    CommentList.prototype.likeClick = function () {
        $.ajax({
            type: "PUT",
            url: "data/like/up/" + this.index.toString(),
            data: JSON.stringify({ "index": this.index, "title": this.title, "comment": this.comment, "uploadDate": this.uploadDate, "lastLikeDate": Date.now() }),
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
    /**
     * updates a comment's like # and like date via a PUT route.
     * shows an error if it cannot do so, otherwise reloads the page via getDataFromServer
     */
    CommentList.prototype.dislikeClick = function () {
        $.ajax({
            type: "PUT",
            url: "data/like/down/" + this.index.toString(),
            data: JSON.stringify({ "index": this.index, "title": this.title, "comment": this.comment, "uploadDate": this.uploadDate, "lastLikeDate": Date.now() }),
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
    /**
     *
     * @returns {html code to display CommentList on a web page}
     */
    CommentList.prototype.commentDisplay = function () {
        var source = $("#template_1").html();
        var template = Handlebars.compile(source);
        var context = { title: this.title,
            comment: this.comment,
            likes: this.likes,
            uploadDate: this.uploadDate,
            lastLikeDate: this.lastLikeDate };
        var html = template(context);
        return html;
    };
    return CommentList;
}());
/**
 *requests data from db as a json object, resets the html, and turns json data into an array of commentList objects and displays them
 */
function getDataFromServer() {
    //clear html
    document.getElementById("listing").innerHTML = "";
    var cBox = new CommentBox();
    document.getElementById("commentBox").innerHTML = cBox.boxDisplay(); //display comment box
    /**
     * uses GET route to obtain data from db as JSON.
     * if possible, turns data into an array of commentList objects and displays them in reverse chronological order
     * if not, sends an error
     */
    $.ajax({
        type: "GET",
        url: "/data",
        dataType: "json",
        success: function (data) {
            $.each(data, function(index, element) {
                var likes = element.likes;
                var cl = new CommentList(element.index, element.title, element.comment, element.numLikes, element.uploadDate, element.lastLikeDate);
                document.getElementById("listing").innerHTML += cl.commentDisplay();
            });
        },
        error: function () {
            window.alert("something wrong with success:function(data) in getDataFromServer");
        }
    });
}
//calls getDataFromServer to initialize page
getDataFromServer();
