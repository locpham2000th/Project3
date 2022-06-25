$(document).ready(function(){
    $("#submit").click(function (){
        let name = $("input[name=username]").val()
        let email = $("input[name=email]").val()
        let password = $("input[name=password]").val()
        $.ajax({
            type: "POST",
            url: "api/register",
            data: {
                name : name,
                email: email,
                password: password
            },
            success: function (response) {
                alert(response)
            },
            error: function () {
                alert("Đã tồn tại email này")
            }

        })
    })
    $("#login").click(function (){
        window.location = `/`
    })
})