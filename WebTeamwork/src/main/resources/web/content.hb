<div class="panel panel-default">
<div class="panel-heading">
<h3 class="panel-title">Here's your data:</h3>
</div>
<div class="panel-body">
<ul>
{{! 'array', because we are going through each element of the field}}
{{! called 'array'}}
{{#each array}}
<li>
{{! 'data', because each array entry is an object with a field called {{! 'data'}}
{{this.data}}
</li>
{{/each}}
</ul>
</div>
</div>