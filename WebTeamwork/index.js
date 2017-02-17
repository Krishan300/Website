/**
 * Created by Robert Salay on 2/12/2017.
 */
Handlebars.registerHelper('body');
{
    var CommentBox_1 = (function () {
        function CommentBox_1() {
        }
        CommentBox_1.prototype.boxDisplay = function () {
            return "<input id='newTitle' type='text' /> <textarea id='newComment' rows='4' cols='50' maxlength='144'>"
                + "Share your thoughts with the world! 144 characters max, please. </textarea>" +
                "<button id='sendComment' onclick='sendComment()'>Send Comment</button>";
        };
        CommentBox_1.prototype.sendComment = function () {
            var sendData = { "title": document.getElementById("newTitle"), "comment": document.getElementById("newComment"),
                "likes": 0, "uploadDate": Date.now(), "likeDate": Date.now() };
            //add ok check
            getDataFromServer();
        };
        return CommentBox_1;
    }());
    var Comment_1 = (function () {
        function Comment_1(index, title, comment, likes, uploadDate, likeDate) {
            this.index = index;
            this.title = title;
            this.comment = comment;
            this.likes = likes;
            this.uploadDate = uploadDate;
            this.likeDate = likeDate;
        }
        Comment_1.prototype.commentDisplay = function () {
            return "<hr> <h1>" + this.title + "</h1>"
                + "<p>" + this.comment + "</p><br>"
                + "<button id='like' onclick='likeClick()'>Like</button>"
                + this.likes + "<button id='dislike' onclick='dislikeClick()'>Dislike</button><br>"
                + "<p>This comment was posted on </p>" + this.uploadDate
                + "<p> and was last liked on </p>" + this.likeDate + "<br>";
        };
        Comment_1.prototype.likeClick = function () {
            var likeData = { "index": this.index, "title": this.title, "comment": this.comment, "likes": this.likes + 1, "uploadDate": this.uploadDate, "likeDate": Date.now() }; // add update, turn like/dis to full datum obj
            //add ok check
            getDataFromServer();
        };
        Comment_1.prototype.dislikeClick = function () {
            var likeData = { "index": this.index, "title": this.title, "comment": this.comment, "likes": this.likes - 1, "uploadDate": this.uploadDate, "likeDate": Date.now() }; //change to route w/o like #
            //add ok check
            getDataFromServer();
        };
        return Comment_1;
    }());
    function getDataFromServer() {
        var allData = getAllData(); //pretty sure this function is backend's job
        document.body.innerHTML = "";
        document.body.innerHTML = "<h1 align='center'>The Buzz:</h1>  <h2 align='center'>The newest social media out there!</h2> <hr>";
        var a = new CommentBox_1();
        document.body.innerHTML += a.boxDisplay();
        var b = new Comment_1[allData.length];
        for (var i in allData) {
            var counter = 1;
            b[allData.length - counter](allData[i].index, allData[i].title, allData[i].comment, allData[i].likes, allData[i].uploadDate, allData[i].likeDate);
            counter++;
        }
        for (var _i = 0, b_1 = b; _i < b_1.length; _i++) {
            var j = b_1[_i];
            document.body.innerHTML += j.commentDisplay();
        }
    }
    getDataFromServer();
}
