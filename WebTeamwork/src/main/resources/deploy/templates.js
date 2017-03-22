(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['content.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    return "<li>\n"
    + container.escapeExpression(container.lambda((depth0 != null ? depth0.data : depth0), depth0))
    + "\n</li>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\">\n<div class=\"panel-heading\">\n<h3 class=\"panel-title\">Here's your data:</h3>\n</div>\n<div class=\"panel-body\">\n<ul>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.array : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "</ul>\n</div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['nav.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "\n<nav id=\"nav\" class=\"navbar navbar-default navbar-fixed-top\">\n<div class=\"container-fluid\">\n\n<div class=\"navbar-header\">\n <button type=\"button\" class=navbar-toggle collapsed\"\n    data-toggle=\"collapse\"\n    data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n<span class=\"sr-only\">Toggle navigation</span>\n<span class=\"icon-bar\"></span>\n<span class=\"icon-bar\"></span>\n<span class=\"icon-bar\"></span>\n</button>\n<a class=\"navbar-brand\" href=\"#\">CSE 216</a>\n</div>\n\n<div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n<ul class=\"nav navbar-nav\">\n<li><a id=\"navWelcomeBtn\" href=\"#\">Welcome</a></li>\n<li><a id=\"navContentBtn\" href=\"#\">Content</a></li>\n</ul>\n</div>\n</div>\n</nav>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['commentBox.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var helper;

  return "\n<h1>commentBox</h1>\n<input class=\"commentInput\" id=\"newComment\" type=\"textarea\"  />\n<input class=\"commentInput\" id=\"commentArea\" type=\"textarea\" name=\"newComment\"/>\n<commentBox-body>\n"
    + container.escapeExpression(((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : {},{"name":"body","hash":{},"data":data}) : helper)))
    + "\n<button class=\"commentButton\" id=\"sendComment\"> Send Comment</button>\n\n\n\n\n";
},"useData":true});
})();
