
class nav {

 public static init() {
   $("indexNav").html(Handlebars.templates['nav.hb']());
   $("#navContentBtn").click(nav.onContentClick);
   $("navProfileBtn").click(nav.onProfileClick);
}

private static highlight(which: any) {
 $("indexNav li").removeClass("active");
 $(which).parent().addClass("active");
}

public static onContentClick() {
  nav.highlight(this);
  content.getAndShow();

}

//what happens when you click on profile button
public static onProfileClick(){
 nav.highlight(this);
 profile.displayInfo();
}

}




 