// Alex Van Heest's Frontend Testing

// NOTE: My ability to test the frontend was entirely contingent on the code from phase1 actually working. Since it didn't
// work to some degree until Thursday night, I didn't have time to write out much more than a few simpler tests. Since I'll
// be picking up frontend for phase 3, I'll be able to write a few more detailed tests as I go along. And since phase 2 isn't
// working in any form yet, I decided against writing more than a loose overview of how I might test for that in the future.
// Again, sorry for the lack of depth here, but it will be made up for when I can actually write code for this portion myself.

// Check that boxDisplay method returns correct HTML code. Very simple initial test.
describe("boxDisplay()", function() {
	it("returns some HTML code", function() {
		var cBox = new CommentBox();
		expect(cBox.boxDisplay()).toEqual("<input id='newTitle' size='50' maxlength='50' type='text' value='A title for your post! 50 characters max.'>"
            + " </input><br>"
            + "<textarea id='newComment' rows='4' cols='50' maxlength='144'>"
            + "Share your thoughts with the world! 144 characters max, please. </textarea>"
						  + "<button type=button onclick='CommentBox.prototype.sendComment()'>Send Comment</button>");
	    });
    });

// Check whether or not CommentList constructor properly sets the object's variables.
// NOTE: CommentList is poorly named! It's not a CommentList, it's actually a single Comment.
describe("CommentList()", function() {
	it("creates a CommentList object with expected fields", function() {
		var myDate = Date.now();
		var cList = new CommentList(1, "My Title", "Some Content", 3, myDate, myDate);
		expect(cList.index).toEqual(1);
		expect(cList.title).toEqual("My Title");
		expect(cList.comment).toEqual("Some Content");
		expect(cList.likes).toEqual(3);
		expect(cList.uploadDate).toEqual(myDate);
		expect(cList.lastLikeDate).toEqual(myDate);
	    });
    });

// Checking like requires a working version of the backend with the frontend for our current implementation.
// Checking dislike requires a working version of the backend with the frontend for our current implementation.
// Checking getDataFromServer requires a working version of the backend with the frontend for our current implementation.
// Checking sendComment requires a working version of the backend with the frontend for our current implementation.
// We don't have anything in the CommentBox constructor, so there's nothing to test there.

// Check whether or not commentDisplay works properly. It's supposed to build the items in the list to work with the
// Handlebars template in the HTML file.
// NOTE: Need to install jasmine-ajax in the WebBackend folder in order for this to work properly.
describe("commentDisplay()", function () {
	it("displays the comment using the given Handlebars template", function() {
		var myDate = Date.now();
		var cList = new CommentList(1, "My Title", "Some Content", 3, myDate, myDate);
		window.alert(cList.commentDisplay());
		expect(cList.commentDisplay()).toEqual("<h1>My Title</h1><p>Some Content</p>" + 
        "<button id='like' onclick='CommentList.prototype.likeClick()'>Like</button>   3   <button id='dislike' onclick='CommentList.prototype.dislikeClick()'>Dislike</button><br>" + 
						       "<p>This comment was posted on " + myDate + " and was last liked on " + myDate + "</p><hr>");
	    });
    });


// How to get caught up with phase 2 tests:
// I will need to implement tests to check:
// - the flow of user actions to ensure that they logically make sense (need to design some flowchart possibly)
// - login page tests (can a user login? is the page reachable when logged in? does the user get a token?)
// - logout options (can a user logout? is the page reachable when logged out? what happens if a user is automatically logged out?)
// - registration options (case issues handled on backend, but can some be reported on frontend? is SendGrid being used?)
// - up vote / down vote (are they counted? can they be neutralized? should be able to test now but we still don't have a working 
// version of the frontend-backend working...)
// - commenting (can a user comment? is their username recorded? what happens if a user tries to comment without being logged in?)
// - profile pages (do users have profile pages? do they have an editable bio field? do they display relevant posts in body of page?)
// - Bootstrap nav bar (does it display? logged in/ logged out appearance?)
// - SendGrid performance (does it send properly? perhaps these are manual tests with some notes about results...)
// - admin app interface (does it display properly and work with backend? are registration requests available to accept/reject?)
// There are probably some more considerations, but considering that we don't have much frontend at the moment, I'm not sure what these
// are yet.