/**
 * Created by Robert Salay on 2/12/2017.
 */
    class CommentBox{
        constructor(){

        }

        //display comment creation box
        boxDisplay(){
            return "<input id='newTitle' size='50' maxlength='50' type='text' value='A title for your post! 50 characters max.'>"
            + " </input>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>" +
            "<button id='sendComment' onclick='sendComment()'>Send Comment</button>"
        }

        //sends custom comment and title to db, checks for null
        sendComment(){
            var title = document.getElementById("newTitle").textContent;
            var comment = document.getElementById("newComment").textContent;
            title = (title === "") ? null : title;
            comment = (comment === "") ? null : title;
            $.ajax({
                type: "POST",
                url: "/data",
                data: JSON.stringify({"title":title,
                    "comment":comment,
                    "likes":0, "uploadDate":Date.now(), "likeDate":Date.now()}),
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
        }

    }
    class CommentList{
        constructor(public index: number,
                    public title: string,
                    public comment: string,
                    public likes: number,
                    public uploadDate: Date,
                    public likeDate: Date) {
        }

        //displays comment
        commentDisplay() {
            return "<hr> <h1>" + this.title + "</h1>"
                + "<p>" + this.comment + "</p><br>"
                + "<button id='like' onclick='likeClick()'>Like</button>"
                + this.likes + "<button id='dislike' onclick='dislikeClick()'>Dislike</button><br>"
                + "<p>This comment was posted on </p>" + this.uploadDate
                + "<p> and was last liked on </p>" +  this.likeDate + "<br>";
        }

        //sends message to db about specific comment being liked
        likeClick() {
            $.ajax({
                type: "PUT",
                url: "data/like/" + this.index + "/upvote",
                data: JSON.stringify({"index":this.index, "title":this.title, "comment":this.comment, "uploadDate":this.uploadDate, "likeDate":Date.now()}),
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
        }
        //sends message to db about specific comment being disliked
        dislikeClick() {
            $.ajax({
                type: "PUT",
                url: "data/like/" +this.index +"/downvote",
                data: JSON.stringify({"index":this.index, "title":this.title, "comment":this.comment, "uploadDate":this.uploadDate, "likeDate":Date.now()}),
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
        }
    }
    function getDataFromServer() {
        //clear html
        document.getElementById("header").innerHTML = "";
        document.getElementById("commentBox").innerHTML = "";
        document.getElementById("listing").innerHTML = "";

        var a = new CommentBox();
        document.getElementById("commentBox").innerHTML = a.boxDisplay(); //display comment box
        $.ajax({
            type: "GET",
            url: "/data",
            dataType: "json",
            success: function (data) {
                var b = new CommentList[data.length];
                //turn db data into comments stored in reverse chronological order
                for (var i in data) {
                    var counter = 1;
                    b[data.length - counter] = new CommentList(data[i].index, data[i].title, data[i].comment, data[i].likes, data[i].uploadDate, data[i].likeDate);
                    counter++;
                }
                //display all comments
                for (var _i = 0, b_1 = b; _i < b_1.length; _i++) {
                    var j = b_1[_i];
                    document.getElementById("listing").innerHTML += j.commentDisplay();
                }
            }
        });

    }
    document.getElementById("header").innerHTML = "<h1 align='center'>The Buzz:</h1>  <h2 align='center'>The newest social media out there!</h2> <hr>";
    getDataFromServer();