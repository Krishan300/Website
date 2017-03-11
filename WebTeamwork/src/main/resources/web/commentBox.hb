{{! The is a bootstrap commentBox, configured to be at the top of the page }}
{{! The Id is 'commentBox', and anything in it we will need to find by ID also has an ID of commentBox }}

<h1>commentBox</h1>
<input class="commentInput" id="newComment" type="textarea"  />
<input class="commentInput" id="commentArea" type="textarea" name="newComment"/>
<commentBox-body>
{{body}}
<button class="commentButton" id="sendComment"> Send Comment</button>




