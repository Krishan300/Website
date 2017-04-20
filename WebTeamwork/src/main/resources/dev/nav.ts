/// The navigation bar has two buttons, one for the 'Welcome' page and one for the 'Content' page

class nav {
	/// Put the navbar onto the page and configure its buttons
	public static init() {
		$("#indexNav").html(Handlebars.templates['nav.hb']({
			account: nav.loggedIn()
		}));
		$("#navContentBtn").click(nav.onContentClick);
		$("#navWelcomeBtn").click(nav.onWelcomeClick);
		$("#navAdminBtn").click(nav.onAdminClick);
	}

	/// Manage the highlighting of whichever button corresponds
	/// to the active content on the page
	private static highlight(which: any) {
		$("#indexNav li").removeClass("active");
		$(which).parent().addClass("active");
	}

	/// Handle a click of the "Content" button
	public static onContentClick() {
		// NB: 'this' is whatever DOM element was clicked
		nav.highlight(this)
		content.getAndShow();
	}

	/// Handle a click of the "Welcome" button
	public static onWelcomeClick() {
		nav.highlight(this);
		welcome.putInDom();
	}

	// Handle a click of the "Admin Login" button
	public static onAdminClick(){
		nav.highlight(this);
		//should take to andmin page which displays deletable content if user is admin
		content.getAndShowDeletable();
	}

	public static loggedIn() {
		if (document.cookie === "") {
			return "Log in";
		}
		else {
			return welcome.getCookieValue("user");
		}
	}
}