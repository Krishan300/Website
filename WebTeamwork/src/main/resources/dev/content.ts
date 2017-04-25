/// The content object is responsible for filling the 'indexMain'
/// div with the result of an AJAX query to get the most recent data.
class content {
	/// getAndShow will request some data from the server via AJAX call.
	/// When data arrives, it will be passed to show()
	///
	/// NB: remember that every file request is a GET, so we can
	/// put 'data.json' in the file hierarchy, and then GET it as
	/// a way of mocking a REST route.
	///
	/// I cannot do this until the backend is up and running. The backend will provide
	/// some route I can use to get a certain article in realtime. Until then, use dummy text.
	/// I have created a version of the backend (based on a round of backend code finished during
	/// our two days of catch-up after phase 1) that I will base these actions on. I didn't want to 
	/// even vaguely appear to be cheating, so this version of the backend won't be uploaded until after the due date
	/// has passed by at least a day. Our backend developer for phase 3 didn't even get the backend converted
	/// to POSTGRES, which should've been done during phase 2 but wasn't, so I had to do this in order to test
	/// anything in my frontend. All in all, it's been a really long uphill battle, but I want to make it clear
	/// that under no circumstances did I share the backend code with ANYONE in the group, especially our backend
	/// developer, and that I did it not in the interest of phase 3 as a whole but in the interest of having something
	/// to test my frontend with. My apologies if this isn't allowed, I just didn't want to submit a long apology about
	/// why I couldn't do anything this week and I figured I'd at least try to build the routes phase 2 should've had,
	/// even if we're far from using OAuth on the server level. (This is of course with the added difficulty that nothing functional
	/// was finished from frontend during phase 2, so I was starting from phase 1 frontend code that I had written a long
	/// time ago as well. Yes, it's surely been an adventure.)
	///
	public static getAndShow() {
		///document.getElementById("#indexMain").innerHTML = "";
		$.ajax({
			method: "GET",
			url: "/data",
			dataType: "json",
			success: content.show
		});
	}

	//needs to be updated for admin to be able to delete data
    //need to add another route
	public static getAndShowDeletable() {
		///document.getElementById("#indexMain").innerHTML = "";
		$.ajax({
			method: "GET",
			url: "/data",
			dataType: "json",
			success: content.show
		});
	}

	/// When the data arrives from the server, render the content.hb
	/// template using the data.
	private static show(data: any) {
		if (data.res === "bad data") {
			$("#indexMain").html("Please log in to view content!");
		}
		else {
			$("#indexMain").html(Handlebars.templates['content.hb'](data));
		}
	}

	public static sendComment() {
		var myTitle = $("#comment-title").val();
        var myBody = $("#comment-body").val();
        myTitle = (myTitle === "") ? null : myTitle;
        myBody = (myBody === "") ? null : myBody;

        $.ajax({
            type: "POST",
            url: "/data",
            data: JSON.stringify({ user_id: 1, title: myTitle, body: myBody }),
            dataType: "json"
        }).done( function (data, status) {
            if (data.res === "ok") {
                this.getAndShow();
            }
            else {
                window.alert("Invalid input provided (title and comment cannot be empty)");
            }
        }).fail( function (errortype, data, status) {
            window.alert("Send comment failed");
        });
	}




    public static sendPDF(){

    }


}












