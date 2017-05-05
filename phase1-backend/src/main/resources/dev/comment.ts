class comment {

public static getAndShow(idx){
$.ajax({
     method:"GET",
     url:"/data/message/comment/show/"+idx,
     dataType:"json",
     success: comment.show
});
}


private static show(data:any){
 $("#indexMain").html(Handlebars.templates['comment.hb'](data));

}

}