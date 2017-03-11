// This  is a typescript Login page

class login {

    public static init(){
        $("indexLogin").html(Handlebars.templates['login.hb']);
        $("loginSubmitButton").click(login.sendUsername());


    }

    /**sends password and username to backend
     *
     */
    public static sendUsername(){


            //takes user name and password and sends it to backend via post route
            var usernameinput=$("username").val();
            var passwordinput=$("password").val();
            $.ajax({
                type:"POST",
                url: "/data/pw/",
                data: JSON.stringify({usernameinput, passwordinput}),
                dataType:"json",
                success: function(data){
                    if(data.res=="ok") {
                        getJsonFromServer();

                    }
                    else {
                        window.alert("Have you forgot your password?");

                    }

                }

            });



     /**Retrieves data from backend. If data is retrieved, the password and username were authenticated */

        function getJsonFromServer() {
            $.ajax({
                type: "GET", //get route
                url: "/data/pw/vald", //url between frontend and backend
                data: JSON,
                dataType: "json",

                success: function (data) {
                    if (data.res == "ok") {

                        //CommentBox.boxDisplay();
                    }
                    else {
                        window.alert("username or password is not valid");

                    }
                }
            });

        }





    }







}