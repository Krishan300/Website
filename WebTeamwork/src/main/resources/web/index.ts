/**
 * Created by Robert Salay on 2/12/2017.
 */
    class CommentBox{
        constructor(){

        }

        boxDisplay(){
            return "<input id='newTitle' size='50' maxlength='50' type='text'>"
            + "A title for your post! 50 characters max. </input>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>" +
            "<button id='sendComment' onclick='sendComment()'>Send Comment</button>"
        }

        sendComment(){
            $.ajax({
                type: "POST",
                url: "/data",
                data: JSON.stringify({"title":document.getElementById("newTitle"),
                    "comment":document.getElementById("newComment"),
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

        commentDisplay() {
            return "<hr> <h1>" + this.title + "</h1>"
                + "<p>" + this.comment + "</p><br>"
                + "<button id='like' onclick='likeClick()'>Like</button>"
                + this.likes + "<button id='dislike' onclick='dislikeClick()'>Dislike</button><br>"
                + "<p>This comment was posted on </p>" + this.uploadDate
                + "<p> and was last liked on </p>" +  this.likeDate + "<br>";
        }

        likeClick() {
            $.ajax({
                type: "PUT",
                url: "/data/upvote/" + this.index,
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

        dislikeClick() {
            $.ajax({
                type: "PUT",
                url: "/data/downvote/" + this.index,
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
            getDataFromServer()
        }
    }
    function getDataFromServer() {
        document.body.innerHTML = "";
        document.body.innerHTML = "<h1 align='center'>The Buzz:</h1>  <h2 align='center'>The newest social media out there!</h2> <hr>"
        var a = new CommentBox();
        document.body.innerHTML += a.boxDisplay();
        $.ajax({
            type: "GET",
            url: "/data",
            dataType: "json",
            success: function(data) {
                var b = new Comment[data.length];

                for(let i in data) {
                    var counter = 1;
                    b[data.length - counter] = new CommentList(data[i].index, data[i].title, data[i].comment, data[i].likes, data[i].uploadDate, data[i].likeDate);
                    counter++;
                }

                for(let j of b) {
                    document.body.innerHTML += j.commentDisplay();
                }
            }
        });

    }

    getDataFromServer();