$(document).ready(function(){
    let login = $(".login")
    login.click(function(){
        var email = $("#email").val();
        var password = $("#password").val();
        console.error(email, password);
        $.post(
        "/login",
        {
            email: email,
            password: password
        },
        function(response){
            let id = response.id
            window.location = `/`
        })
    })

    $("#password").keypress(function (event){
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            var email = $("#email").val();
            var password = $("#password").val();
            console.error(email, password);
            $.post(
                "/login",
                {
                    email: email,
                    password: password
                },
                function(response){
                    let id = response.id
                    window.location = `/`
                })
        }
    })

})