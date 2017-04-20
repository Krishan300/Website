/// The content object is responsible for filling the 'indexMain'
/// div with the result of an AJAX query to get the most recent data.
class profile {
	/// getAndShow will request some data from the server via AJAX call.
	/// When data arrives, it will be passed to show()
	/// NOTE: This is for one profile page.
	///
	public static getAndShow(idx) {
		$.ajax({
			method: "GET",
			url: "/user/" + idx,
			dataType: "json",
			success: profile.show
		});
	}

	/// When the data arrives from the server, render the content.hb
	/// template using the data.
	/// NOTE: Using dummy text here. Will replace with values received from backend once it works at all.
	private static show(data: any) {
		$("#indexMain").html(Handlebars.templates['profile.hb'](data));
	}
}