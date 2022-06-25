$(document).ready(function(){
    let login = $(".login")
    // login.click(function(){
    //     console.error("đã nhận sk click login");
    //     var email = $("#email").val();
    //     var password = $("#password").val();
    //     console.error(email, password);
    //     $.ajax({
    //         type: "POST",
    //         url: "/login",
    //         dataType: "json",
    //         data: {
    //             email: email,
    //             password: password
    //         },function(result){
    //             console.error(result);
    //         }
    //         // error: function (response) {
    //         //     console.error(response);
    //         //     window.location = "/home/1"
    //         // }
    //     })
    // })
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
})