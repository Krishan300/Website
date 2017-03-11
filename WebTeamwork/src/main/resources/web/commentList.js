/**
 * Created by krishanmadan on 3/10/17.
 */
var commentList = (function () {
    function commentList(index, title, comment, likes, uploadDate, likeDate) {
        this.index = index;
        this.title = title;
        this.comment = comment;
        this.likes = likes;
        this.uploadDate = uploadDate;
        this.likeDate = likeDate;
    }
    commentList.init = function () {
        $("index").html(Handlebars.templates['commentList.hb']());
    };
    commentList.putInDom = function () {
    };
    commentList.prototype.commentDisplay = function () {
        return "<hr> <h1>" + this.title + "</h1>"
            + "<p>" + this.comment + "</p><br>"
            + "<button id='like' onclick='likeClick()'>Like</button>"
            + this.likes + "<button id='dislike' onclick='dislikeClick()'>Dislike</button><br>"
            + "<p>This comment was posted on </p>" + this.uploadDate
            + "<p> and was last liked on </p>" + this.likeDate + "<br>";
    };
    commentList.prototype.likeClick = function () {
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
    /**
     * updates a comment's like # and like date via a PUT route.
     * shows an error if it cannot do so, otherwise reloads the page via getDataFromServer
     */
    commentList.prototype.dislikeClick = function () {
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
    return commentList;
}());
/**
 *requests data from db as a json object, resets the html, and turns json data into an array of commentList objects and displays them
 */
function getDataFromServer() {
    //clear html
    document.getElementById("commentBox").innerHTML = "";
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
            if (data.res === "okay") {
                var cList = new CommentList[data.length];
                //turn db data into comments stored in reverse chronological order
                var counter = 1;
                for (var i in data) {
                    cList[data.length - counter] = new CommentList(data[i].index, data[i].title, data[i].comment, data[i].likes, data[i].uploadDate, data[i].lastlikeDate);
                    counter++;
                }
                //display all comments
                for (var k = 0; k < cList.length; k++) {
                    document.getElementById("listing").innerHTML += cList[k].commentDisplay();
                }
            }
            else {
                window.alert("Cannot get web data at this time. Try again later.");
            }
        }
    });
}
