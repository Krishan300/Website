{{! This panel shows the content for a selected profile page. The 'this' will need to be replaced. }}
<div class="panel panel-default">
	{{! NOTE: This is a rushed implementation, so the following for-each loop is not best practice at all since we
	    only expect this to be one comment. I did it like this to accommodate my hasty experimental (private) backend build
	    that I had to make from phase 1 in order to have anything to test for phase 3. -- Alex}}
	{{#each this}}
		<div class="panel-heading">
			<h3 class="panel-title">{{this.realname}}'s Profile</h3>
		</div>
		<div class="panel-body">
			<div class="article-body">
				Username: {{this.username}}<br>
				Realname: {{this.realname}}<br>
				Email:    {{this.email}}<hr>
			</div>
			{{! TBD: Include all messages and comments here}}
			<div class="profile-posts">
				All Messages:<br><hr>
			</div>
			<div class="profile-comments">
				All Comments:<br><hr>
			</div>
			<a id="article-back-btn" onclick="content.getAndShow()">&larr; Return to Content Listing</a>
		</div>
	{{/each}}
</div>