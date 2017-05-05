(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['content.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "			<li>\n				<h3><a onclick=\"article.getAndShow("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")\">"
    + alias2(alias1((depth0 != null ? depth0.title : depth0), depth0))
    + "</a></h3>  written by <a onclick=\"profile.getAndShow("
    + alias2(alias1((depth0 != null ? depth0.user_id : depth0), depth0))
    + ")\">"
    + alias2(alias1((depth0 != null ? depth0.realname : depth0), depth0))
    + "</a><br><br>\n			</li><hr>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.lambda, alias2=container.escapeExpression;

  return "<div class=\"panel panel-default\">\n	<div class=\"panel-heading\">\n		<h3 class=\"panel-title\">Recent posts</h3>\n	</div>\n	<div class=\"panel-body\">\n		<div id=\"commentBox\"><input id='comment-title' cols='50' type='text' placeholder=\"Test input\"></input>\n			<br><textarea id='comment-body' rows='4' cols='50' maxlength='144' placeholder=\"Type message here!\"></textarea><br>\n			<button id=\"comment-send-btn\" onclick=\"content.sendComment()\">Send Comment</button>\n		</div>\n		<ul id=\"article-list\">\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "		</ul>\n	</div>\n</div>\n<!-- Votes: <button id='upvote' onclick='content.upvote("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")'>Up</button>   "
    + alias2(alias1((depth0 != null ? depth0.tot_votes : depth0), depth0))
    + "   <button id='downvote' onclick='content.downvote("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")'>Down</button> -->";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['nav.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var helper;

  return "<nav id=\"nav\" class=\"navbar navbar-default navbar-fixed-top\">\n	<div class=\"container-fluid\">\n		<div class=\"navbar-header\">\n			<button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n				<span class=\"sr-only\">Toggle navigation</span>\n				<span class=\"icon-bar\"></span>\n				<span class=\"icon-bar\"></span>\n				<span class=\"icon-bar\"></span>\n			</button>\n			<a class=\"navbar-brand\" href=\"#\">Luna's Phase 3</a>\n		</div>\n\n		<div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n			<ul class=\"nav navbar-nav\">\n				<li><a id=\"navWelcomeBtn\" href=\"#\">"
    + container.escapeExpression(((helper = (helper = helpers.account || (depth0 != null ? depth0.account : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : {},{"name":"account","hash":{},"data":data}) : helper)))
    + "</a></li>\n				<li><a id=\"navContentBtn\" href=\"#\">Content</a></li>\n			</ul>\n		</div>\n	</div>\n</nav>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['welcome.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function";

  return "<div class=\"panel panel-default\">\n	<div class=\"panel-heading\">\n		<h3 class=\"panel-title\">"
    + container.escapeExpression(((helper = (helper = helpers.pagetitle || (depth0 != null ? depth0.pagetitle : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pagetitle","hash":{},"data":data}) : helper)))
    + "</h3>\n	</div>\n	<div class=\"panel-body\">\n		<div class=\"pagecontent\">\n			"
    + ((stack1 = ((helper = (helper = helpers.pagecontent || (depth0 != null ? depth0.pagecontent : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pagecontent","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\n		</div>\n	</div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['article.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.lambda, alias2=container.escapeExpression;

  return "		<div class=\"panel-heading\">\n			<h3 class=\"panel-title\">"
    + alias2(alias1((depth0 != null ? depth0.title : depth0), depth0))
    + "</h3>\n		</div>\n		<div class=\"panel-body\">\n			<div class=\"article-body\">\n				"
    + alias2(alias1((depth0 != null ? depth0.body : depth0), depth0))
    + "<hr>\n			</div>\n			<div class=\"article-author\">\n				Written by <a onclick=\"profile.getAndShow("
    + alias2(alias1((depth0 != null ? depth0.user_id : depth0), depth0))
    + ")\">"
    + alias2(alias1((depth0 != null ? depth0.realname : depth0), depth0))
    + "</a><br>\n			</div>\n			<div class=\"article-votes\">\n				Votes: <button id='upvote' onclick='article.upvote("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")'>Up</button>   "
    + alias2(alias1((depth0 != null ? depth0.tot_votes : depth0), depth0))
    + "   <button id='downvote' onclick='article.downvote("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")'>Down</button><br><br>\n			</div>\n			<a id=\"article-back-btn\" onclick=\"content.getAndShow()\">&larr; Return to Content Listing</a>\n\n            <br>\n			<a id=\"article-delete-btn\" onclick=\"article.deleteMessage("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")\">delete post</a>\n            </br>\n\n\n\n\n<div id=\"commentBox\">\n<input id='comment title' cols='50' placeholder=\"Your comment title\">\n</input><br>\n<textarea id='comment-body' rows='4' cols='50' maxlength='144' placeholder=\"Type message here!\"></textarea><br>\n<button id=\"comment-send-btn\" onclick=\"article.sendComment("
    + alias2(alias1((depth0 != null ? depth0.user_id : depth0), depth0))
    + ", "
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")\">Send Comment</button>\n<hr>\n<button id=\"display-comments-btn\" onclick=\"article.callCommentGetAndShow("
    + alias2(alias1((depth0 != null ? depth0.message_id : depth0), depth0))
    + ")\">Comments</button>\n<hr>\n\n\n\n"
    + ((stack1 = helpers["if"].call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.movietitle : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.program(15, data, 0),"data":data})) != null ? stack1 : "")
    + "\n</div>\n\n";
},"2":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=depth0 != null ? depth0 : {};

  return "<div class=\"movie-infobox\">\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.movietitle : depth0),{"name":"if","hash":{},"fn":container.program(3, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n\n\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.year : depth0),{"name":"if","hash":{},"fn":container.program(5, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.imdbid : depth0),{"name":"if","hash":{},"fn":container.program(7, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.metascore : depth0),{"name":"if","hash":{},"fn":container.program(9, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.runtime : depth0),{"name":"if","hash":{},"fn":container.program(11, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.genres : depth0),{"name":"if","hash":{},"fn":container.program(13, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n</div>\n\n";
},"3":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return " <h3><a href=\"http://ww.imdb.com/title/"
    + alias2(alias1((depth0 != null ? depth0.imdbid : depth0), depth0))
    + "\">"
    + alias2(alias1((depth0 != null ? depth0.movietitle : depth0), depth0))
    + "</a></h3>";
},"5":function(container,depth0,helpers,partials,data) {
    return "<b>Year:</b>"
    + container.escapeExpression(container.lambda((depth0 != null ? depth0.year : depth0), depth0))
    + "<br>";
},"7":function(container,depth0,helpers,partials,data) {
    return "<b>IMDB:</b>"
    + container.escapeExpression(container.lambda((depth0 != null ? depth0.imdbid : depth0), depth0))
    + "<br>";
},"9":function(container,depth0,helpers,partials,data) {
    return "<b>Metascore:</b>"
    + container.escapeExpression(container.lambda((depth0 != null ? depth0.metascore : depth0), depth0))
    + "<br>";
},"11":function(container,depth0,helpers,partials,data) {
    return "<b>Runtime:</b>"
    + container.escapeExpression(container.lambda((depth0 != null ? depth0.runtime : depth0), depth0))
    + "<br>";
},"13":function(container,depth0,helpers,partials,data) {
    return "<b>Genres:<br>"
    + container.escapeExpression(container.lambda((depth0 != null ? depth0.genres : depth0), depth0))
    + "<br>";
},"15":function(container,depth0,helpers,partials,data) {
    return "<div class=\"nomovieinfobox\">\n</div>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\">\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['profile.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "		<div class=\"panel-heading\">\n			<h3 class=\"panel-title\">"
    + alias2(alias1((depth0 != null ? depth0.realname : depth0), depth0))
    + "'s Profile</h3>\n		</div>\n		<div class=\"panel-body\">\n			<div class=\"article-body\">\n				Username: "
    + alias2(alias1((depth0 != null ? depth0.username : depth0), depth0))
    + "<br>\n				Realname: "
    + alias2(alias1((depth0 != null ? depth0.realname : depth0), depth0))
    + "<br>\n				Email:    "
    + alias2(alias1((depth0 != null ? depth0.email : depth0), depth0))
    + "<hr>\n			</div>\n			<div class=\"profile-posts\">\n				All Messages:<br><hr>\n			</div>\n			<div class=\"profile-comments\">\n				All Comments:<br><hr>\n			</div>\n			<a id=\"article-back-btn\" onclick=\"content.getAndShow()\">&larr; Return to Content Listing</a>\n		</div>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\">\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['comment.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "\n			<div class=\"comment-body\">\n				"
    + alias2(alias1((depth0 != null ? depth0.comment_text : depth0), depth0))
    + "<br>\n				Written by <a onclick=\"profile.getAndShow("
    + alias2(alias1((depth0 != null ? depth0.user_id : depth0), depth0))
    + ")\">"
    + alias2(alias1((depth0 != null ? depth0.realname : depth0), depth0))
    + "</a><br>\n			</div>\n\n			<hr>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\">\n		<div class=\"panel-heading\">\n			<h3 class=\"panel-title\">Comments</h3>\n		</div>\n		<div class=\"panel-body\">\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </div>\n</div>";
},"useData":true});
})();
