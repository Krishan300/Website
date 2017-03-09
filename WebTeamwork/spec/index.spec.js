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

/**
 *	The following tests still need to be written:
 *
 * 	CommentBox.sendComment(): look for some return statement after AJAX call?
 * 	CommentList.commentDisplay(): build an example CommentList object and expect the source
 *	getDataFromServer(): look for some return statement?
 *	CommentList.likeClick(): check if CommentList object has an increase in likes? or look for valid return
 *	CommentList.dislikeClick(): opposite my likeClick() solution
 *	CommentBox(): there's nothing currently in our constructor, but shouldn't there be at least something?
 */