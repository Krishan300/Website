class content {
    //requests data from server via an ajax call and passes it to show
    public static getAndShow () {
        $.ajax({
            method: "GET",
            url: "data.json",
            success: content.show
    });

    }

    private static show(data: any) {
        $("indexMain").html(Handlebars.templates['content.hb'](data));

    }
}