
class nav {

 public static init() {
   $("indexNav").html(Handlebars.templates['nav.hb']());
   $("#navContentBtn").click(nav.onContentClick);
   $("navWelcomeBtn").click(nav.onWelcomeClick);
}

private static highlight(which: any) {
 $("indexNav li").removeClass("active");
 $(which).parent().addClass("active");
}

public static onContentClick() {
  nav.highlight(this);
  content.getAndShow();

}

public static onWelcomeClick(){
 nav.highlight(this);
 welcome.putInDom();
}

}




 