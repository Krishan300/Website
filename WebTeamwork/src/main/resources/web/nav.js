var nav = (function () {
    function nav() {
    }
    nav.init = function () {
        $("indexNav").html(Handlebars.templates['nav.hb']());
        $("#navContentBtn").click(nav.onContentClick);
        $("navWelcomeBtn").click(nav.onWelcomeClick);
    };
    nav.highlight = function (which) {
        $("indexNav li").removeClass("active");
        $(which).parent().addClass("active");
    };
    nav.onContentClick = function () {
        nav.highlight(this);
        content.getAndShow();
    };
    nav.onWelcomeClick = function () {
        nav.highlight(this);
        welcome.putInDom();
    };
    return nav;
}());
