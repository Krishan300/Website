{{! This panel shows the content of a selected comment}}
<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Comments</h3>
		</div>
		<div class="panel-body">
		{{#each this}}

			<div class="comment-body">
				{{this.comment_text}}<br>
				Written by <a onclick="profile.getAndShow({{this.user_id}})">{{this.realname}}</a><br>
			</div>

			<hr>
		{{/each}}
        </div>
</div>