{{! This panel shows how to use a handlebars template to show (a) fields of an object, and (b) rows of an array. This expects to receive an object with one field called 'posts', which is an array of posts, each of which has several fields as detailed in the data.json file.}}
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Recent posts</h3>
	</div>
	<div class="panel-body">
		<div id="commentBox"><input id='comment-title' cols='50' type='text' placeholder="Your message title"></input>
			<br><textarea id='comment-body' rows='4' cols='50' maxlength='144' placeholder="Type message here!"></textarea><br>
			<button id="comment-send-btn" onclick="content.sendComment()">Send Comment</button>


        {{!add button to send .pdf files}}
        <input type="file" id="myFileInput" />
        <input type="button" onclick="document.getElementById('myFileInput').click()" value="Select a File" />


		</div>
		<ul id="article-list">
			{{#each this}}
			<li>
				<h3><a onclick="article.getAndShow({{this.message_id}})">{{this.title}}</a></h3>  written by <a onclick="profile.getAndShow({{this.user_id}})">{{this.realname}}</a><br><br>
			</li><hr>
			{{/each}}
		</ul>
	</div>
</div>
<!-- Votes: <button id='upvote' onclick='content.upvote({{this.message_id}})'>Up</button>   {{this.tot_votes}}   <button id='downvote' onclick='content.downvote({{this.message_id}})'>Down</button> -->