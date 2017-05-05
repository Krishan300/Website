/// The content object is responsible for filling the 'indexMain'
/// div with the result of an AJAX query to get the most recent data.
class article {
	/// getAndShow will request some data from the server via AJAX call.
	/// When data arrives, it will be passed to show()
	/// NOTE: This is for one article in particular, not for the whole list. CONTENT is the
	/// area where we get the full listing of messages.
	///
	public static getAndShow(idx) {
		$.ajax({
			method: "GET",
			url: "/data/message/"+idx,
			dataType: "json",
			success: article.show
		});
	}

	/// When the data arrives from the server, render the content.hb
	/// template using the data.
	/// NOTE: Using dummy text here. Will replace with values received from backend once it works at all.
	private static show(data: any) {
		$("#indexMain").html(Handlebars.templates['article.hb'](data));
        //$("indexMain").html(Handlebars.templates['comment.hb'](data));
//add another line comment.hb-look up how two hb files in the same div-or make another div in index.html
	}

	/// Upvote and downvote methods: will be updated once the backend
	/// database is properly created and verifiable in some form.
	public static upvote(idx) {
		///window.alert("You have upvoted an item at index " + x);
		$.ajax({
			method: "POST",
			url: "/data/vote/up",
			/// NOTE: For now, since we have no login functionality, I'm simulating every vote as if done by user at id 1.
			data: JSON.stringify({user_id: 1, message_id: idx}),
			dataType: "json",
			success: article.getAndShow(idx)
		});
	}

	public static downvote(idx) {
		///window.alert("You have downvoted an item at index " + x);
		$.ajax({
			method: "POST",
			url: "/data/vote/down",
			/// NOTE: For now, since we have no login functionality, I'm simulating every vote as if done by user at id 1.
			data: JSON.stringify({user_id: 1, message_id: idx}),
			dataType: "json",
			success: article.getAndShow(idx)
		});
	}

    public static deleteMessage(idx){
      $.ajax({
       method:"POST",
       url: "/data/delete/" + idx,
       dataType: "json",
       success: content.getAndShow
       });

     }

    public static sendComment(user_id, message_id){

        var myBody = $("#comment-body").val();
        myBody = (myBody === "") ? null : myBody;

     $.ajax({
        method: "POST",
        url: "/data/message/comment/" + message_id,
        data: JSON.stringify ({user_id: user_id, message_id:message_id, comment_text: myBody}),
        dataType: "json",
		}).done( function (data, status) {
            if (data.res === "ok") {
                article.getAndShow(message_id);
            }
            else {
                window.alert("Invalid input provided (title and comment cannot be empty)");
            }
        }).fail( function (errortype, data, status) {
            window.alert("Send comment failed");
        });





   }

	public static callCommentGetAndShow(idx) {
		comment.getAndShow(idx);
	}

}