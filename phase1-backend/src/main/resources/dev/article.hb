{{! This panel shows the content for a selected article/post. The 'this' will need to be replaced. }}
<div class="panel panel-default">
	{{! NOTE: This is a rushed implementation, so the following for-each loop is not best practice at all since we
	    only expect this to be one comment. I did it like this to accommodate my hasty experimental (private) backend build
	    that I had to make from phase 1 in order to have anything to test for phase 3. -- Alex}}
	{{#each this}}
		<div class="panel-heading">
			<h3 class="panel-title">{{this.title}}</h3>
		</div>
		<div class="panel-body">
			<div class="article-body">
				{{this.body}}<hr>
			</div>
			<div class="article-author">
				Written by <a onclick="profile.getAndShow({{this.user_id}})">{{this.realname}}</a><br>
			</div>
			<div class="article-votes">
				Votes: <button id='upvote' onclick='article.upvote({{this.message_id}})'>Up</button>   {{this.tot_votes}}   <button id='downvote' onclick='article.downvote({{this.message_id}})'>Down</button><br><br>
			</div>
			<a id="article-back-btn" onclick="content.getAndShow()">&larr; Return to Content Listing</a>

            <br>
            </br>




<div id="commentBox">
<textarea id='comment-body' rows='4' cols='50' maxlength='144' placeholder="Type message here!"></textarea><br>
<button id="comment-send-btn" onclick="article.sendComment({{this.user_id}}, {{this.message_id}})">Send Comment</button>
<hr>
<button id="display-comments-btn" onclick="article.callCommentGetAndShow({{this.message_id}})">Comments</button>
<button id="article-delete-btn" onclick="article.deleteMessage({{this.message_id}})">Delete</button>
<hr>



{{#if this.movietitle}}
<div class="movie-infobox">
{{#if this.movietitle}} <h3><a href="http://www.imdb.com/title/{{this.imdbid}}">{{this.movietitle}}</a></h3>{{/if}}


{{#if this.year}}<b>Year: </b>{{this.year}}<br>{{/if}}
{{#if this.imdbid}}<b>IMDB: </b>{{this.imdbid}}<br>{{/if}}
</div>

{{else}}
<div class="nomovieinfobox">
</div>
{{/if}}

</div>

{{/each}}

</div>