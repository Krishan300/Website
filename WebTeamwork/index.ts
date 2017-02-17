/**
 * Created by Robert Salay on 2/12/2017.
 */
Handlebars.registerHelper('body')
{
    class CommentBox{
        constructor(){

        }

        boxDisplay(){
            return "<input id='newTitle' type='text' /> <textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>" +
            "<button id='sendComment' onclick='sendComment()'>Send Comment</button>"
        }

        sendComment(){
            var sendData = {"title":document.getElementById("newTitle"), "comment":document.getElementById("newComment"), //add post path
                "likes":0, "uploadDate":Date.now(), "likeDate":Date.now()};
            //add ok check
            getDataFromServer()
        }

    }
    class Comment {
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
            var likeData = {"index":this.index, "title":this.title, "comment":this.comment, "likes": this.likes + 1, "uploadDate":this.uploadDate, "likeDate":Date.now()} // add update, turn like/dis to full datum obj
            //add ok check
            getDataFromServer()
        }

        dislikeClick() {
            var likeData = {"index":this.index, "title":this.title, "comment":this.comment, "likes": this.likes - 1, "uploadDate":this.uploadDate, "likeDate":Date.now()} //change to route w/o like #
            //add ok check
            getDataFromServer()
        }
    }
    function getDataFromServer() {
        var allData = getAllData(); //pretty sure this function is backend's job
        document.body.innerHTML = "";
        document.body.innerHTML = "<h1 align='center'>The Buzz:</h1>  <h2 align='center'>The newest social media out there!</h2> <hr>"
        var a = new CommentBox();
        document.body.innerHTML += a.boxDisplay();
        var b = new Comment[allData.length];

        for(let i in allData) {
            var counter = 1;
            b[allData.length - counter] (allData[i].index, allData[i].title, allData[i].comment, allData[i].likes, allData[i].uploadDate, allData[i].likeDate);
            counter++;
        }

        for(let j of b) {
            document.body.innerHTML += j.commentDisplay();
        }
    }

    getDataFromServer();
}