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
			url: "/data/message/" + idx,
			dataType: "json",
			success: article.show
		});
	}

	/// When the data arrives from the server, render the content.hb
	/// template using the data.
	/// NOTE: Using dummy text here. Will replace with values received from backend once it works at all.
	private static show(data: any) {
		$("#indexMain").html(Handlebars.templates['article.hb'](data));
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
     
    public static sendComment(idx){

        var myTitle = $("#comment-title").val();
        var myBody = $("#comment-body").val();
        myTitle = (myTitle === "") ? null : myTitle;
        myBody = (myBody === "") ? null : myBody;

     $.ajax({
        method: "POST",
        url: "/data/message/comment/" + idx,
        data: JSON.stringify ({user_id: 1, message_id: idx, comment_text: myBody}),
        dataType: "json"
     });
    
   }}