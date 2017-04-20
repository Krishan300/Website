{{! This panel shows the content for a selected article/post. The 'this' will need to be replaced. }}
<div class="panel panel-default">
	{{! NOTE: This is a rushed implementation, so the following for-each loop is not best practice at all since we
	    only expect this to be one comment. I did it like this to accommodate my hasty experimental 'private' backend build
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
		</div>
	{{/each}}
</div>