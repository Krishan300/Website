
//displays comments
/* Place to write and display comments */

class commentBox{




    /** Initializes a page with a textbook and input button */

        public static init(){
            $("indexCommentBox").html(Handlebars.templates('commentBox.hb')());
            $("commentButton").click(commentBox.sendComment);



        }


      public boxDisplay(){
            return "<input id='newTitle' size='50' maxlength='50' type='text' value='A title for your post! 50 characters max.'>"
            + " </input>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>"
                + "<button id='sendComment' onclick='sendComment()'>Send Comment</button>"
        }


    /**
     * takes data from textboxes and converts it into a full json object, sent to the db via a POST route
     * shows an error if it can't send the data, otherwise reloads the page via getDataFromServer
     */


        public static sendComment(){

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
        //adds comment to commentList
        public static addtoCommentlist (){
            commentList.putInDom()

        }


        private static highlight(which: any){
            $("commentBox").removeClass("active");
            $("commentBox").removeClass("active");
        }

        //Handle a click of the post message button











    }