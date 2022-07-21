function confirmAddDevice(element) {
    let formControl = $(".form-control")
    let nameDevice = formControl.eq(0).val()
    let kindDevice = formControl.eq(1).val()
    let idPlace = window.location.pathname.slice(7)
    console.error(nameDevice, kindDevice)
    console.error("add device")

    $.ajax({
        url: "/api/device",
        type: "POST",
        dataType: "json",
        data: {
            name: nameDevice,
            idKindDevice: kindDevice,
            idPlace: idPlace
        },
        success: function (){
            alert("Thêm thất bại")
            window.location.reload()
        },
        error: function (error){
            alert("Đã thêm thành công")
            window.location = window.location
            // window.location.reload()
            // alert("Thêm thất bại")
        }
    })

}

function changeStatusOfDevice(id, element) {
    // console.error(id)
    let inputChangeStatus = $(element)
    let active = inputChangeStatus.parent().find(".active")
    let value = inputChangeStatus.parent().find(".value-device")
    $.ajax({
        url: "/api/device/change/" + id,
        type: "PUT",
        success: function (response) {
            active.text(response)
            if (response === true){
                $.ajax({
                    url: "/api/history/first/" + id,
                    type: "GET",
                    success: function (response) {
                        value.text(response.value)
                    }
                })
            }else {
                value.text("-")
            }
        }
    })
}

function deleteDevice(id) {
    $.ajax({
        url: "/api/device/delete/" + id,
        type: "DELETE",
        success: function (){
            window.location = window.location
            alert("Đã xóa thành công")
        }
    })
}

$(document).ready(function (){

    let namePlaceGlobal = $("#name-place")
    let idPlaceGlobal = window.location.pathname.slice(7)
    let idUser = $("#id-user");

    $.ajax({
        url: `/api/place/${idPlaceGlobal}`,
        type: "GET",
        success: function (response) {
            namePlaceGlobal.text(response.name)
            idUser.text(response.user.id)
            initMap(response)
        },
        error: function (response) {
            console.error(response.responseJSON)
            let message = response.responseJSON.message
            if (message === "error.notCookie"){
                alert("Bạn đã hết phiên làm việc")
                window.location = `/`
            }
        }
    })

    let buttonDownOption = $("#button-down-option")
    let bodyOption = $("#body-drop-down-option")
    buttonDownOption.click(function (){
        console.log(bodyOption.css("display"))
        if (bodyOption.css("display") === "none"){
            bodyOption.show()
            bodyOption.css("display", "flex")
        }else {
            bodyOption.hide()
        }
    })
    let logout = $("#logout")
    logout.click(function (){
        $.get(
            "/logout",
            function (){
                window.location = `/`
            }
        )
    })

    let stompClient = null;

    connect()
    function connect() {
        var socket = new SockJS('/mywebsocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.error('Connected: ' + frame);
            console.error("/" + idUser.text() + "/topic/greetings")
            stompClient.subscribe("/user/" + idUser.text() +'/topic/greetings', function (greeting) {
                // console.error(greeting)
                editAlarm(greeting.body);
            });
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.error("Disconnected");
    }

    // setTimeout(sendName, 5000)
    //
    // function sendName() {
    //     console.error("đã send từ client lên server")
    //     stompClient.send("/app/hello", {}, JSON.stringify({'name': "loc"}));
    // }

    function editAlarm(message) {
        message = JSON.parse(message)
        console.error(message)
        let bodyAlarm = $(".body-alarm")
        let bodyTableAlarm = bodyAlarm.eq(0).find("tbody")
        let namePlace = message.namePlace
        console.error(namePlaceGlobal.text())
        if (namePlace === namePlaceGlobal.text()){
            let nameDevice = message.nameDevice
            let title = message.title
            let tr = `<tr><td>${nameDevice}</td><td>${title}</td></tr>`
            bodyTableAlarm.prepend(tr)
        }

    }

    $.ajax({
        url: "/api/KindDevice",
        type: "GET",
        success: function (response) {
            let selectKind = $("#device-kind")
            for (let i = 0; i < response.length; i++){
                let option = `<option value="${response[i].id}">${response[i].name}</option>`
                // console.error(option)
                selectKind.append(option)
            }
        }
    })

    $.ajax({
        url: "/api/device?idPlace=" + idPlaceGlobal,
        type: "GET",
        success: function (response) {
            console.error(response)
            let containDevice = $("#list-device")
            if (response.length > 0){
                let bodyDevice = ``
                for (let i = 0; i < response.length; i++){
                    let row = ``
                    let col = ``
                    let name = response[i].name
                    let id = response[i].id
                    let realTime = response[i].realTime
                    let active = response[i].active === true ? "checked" : ""
                    bodyDevice += `
                        <div class="row list-device-row" style="align-items: center; border-bottom: solid 1px red">
                            <div class="col-8 list-devices-device row" style="align-items: center;">
                                <span class="col-10 list-devices-device-name">${name}<span style="display: none" class="list-devices-device-id-device">${id}</span><span style="display: none" class="real-time">${realTime}</span><span style="display: none" class="active">${response[i].active}</span></span>
                                <span class="col-2 value-device"></span>
                            </div>
                            <div class="form-check form-switch col-2" onclick="changeStatusOfDevice(${id}, this)">
                              <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" ${active} >
                              <label class="form-check-label" for="flexSwitchCheckDefault">on</label>
                            </div>
                            <div class="col-2 button-delete-device" onclick="deleteDevice(${id})" style="padding-right: 0">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--! Font Awesome Pro 6.1.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2022 Fonticons, Inc. --><path d="M576 384C576 419.3 547.3 448 512 448H205.3C188.3 448 172 441.3 160 429.3L9.372 278.6C3.371 272.6 0 264.5 0 256C0 247.5 3.372 239.4 9.372 233.4L160 82.75C172 70.74 188.3 64 205.3 64H512C547.3 64 576 92.65 576 128V384zM271 208.1L318.1 256L271 303C261.7 312.4 261.7 327.6 271 336.1C280.4 346.3 295.6 346.3 304.1 336.1L352 289.9L399 336.1C408.4 346.3 423.6 346.3 432.1 336.1C442.3 327.6 442.3 312.4 432.1 303L385.9 256L432.1 208.1C442.3 199.6 442.3 184.4 432.1 175C423.6 165.7 408.4 165.7 399 175L352 222.1L304.1 175C295.6 165.7 280.4 165.7 271 175C261.7 184.4 261.7 199.6 271 208.1V208.1z"/></svg>
                            </div>
                        </div>
                    `
                }
                containDevice.html(bodyDevice)

                getFirstValue()
                setUpChart()
                setInterval(upDateChart, 10000);


                // setInterval(gethistoryinplace, 10000)
                // gethistoryinplace()
                //ve cac bieu do
                // setUpChart();
                // setInterval(upDateChart, 10000);
            }else {
                containDevice.html("Không có thiết bị nào")
            }
        }
    })

    function getFirstValue() {
        let deviceElement = $(".list-device-row")
        for (let i = 0; i < deviceElement.length; i++){
            let active = deviceElement.eq(i).find(".active").text()
            if(active === "true"){
                let idDevice = deviceElement.eq(i).find(".list-devices-device-id-device")
                $.ajax({
                    url: "/api/history/first/" + idDevice.text(),
                    type: "GET",
                    success: function (response) {
                        deviceElement.eq(i).find(".value-device").text(response.value)
                    }
                })
            }else {
                deviceElement.eq(i).find(".value-device").text("-")
            }
        }

        setInterval(gethistoryinplace, 10000)

    }

    function gethistoryinplace() {
        console.error("helllo")
        let now = new Date()
        let minutes = now.getMinutes()
        let second = now.getSeconds()
        let ids = $(".list-devices-device-id-device")
        for (let i = 0; i < ids.length; i++){
            let id = ids.eq(i).text()
            let realTime = ids.eq(i).next().text()
            let active = ids.eq(i).next().next().text()
            let valueElement = ids.eq(i).parent().next()
            let value = valueElement.text()
            console.error(id, realTime, active, value)
            if (realTime === "true"){
                // dung api lay gia tri
                if (active === "true"){
                    let url = `/api/gethistorydevice?id=${id}`
                    $.get(
                        url,
                        function (response) {
                            console.log(response.value)
                            valueElement.html(response.value)
                        }
                    )
                }else {
                    valueElement.html("-")
                }
            }else {
                if (minutes === 30){
                    if (second < 10){
                        //dung api lay gia tri'
                        if (active === "true"){
                            let url = `/api/gethistorydevice?id=${id}`
                            $.get(
                                url,
                                function (response) {
                                    console.log(response.value)
                                    valueElement.html(response.value)
                                }
                            )
                        }else {
                            valueElement.html("-")
                        }
                    }
                }
            }
            console.log(id, realTime , value)
        }
    }

    $.ajax({
        url: "",
    })

    function initMap(response) {

        console.error(response)

        let lat = response.lat
        let lng = response.lng
        let name = response.name

        let map;
        map = new google.maps.Map(document.getElementById("map"), {
            center: { lat: lat, lng: lng },
            zoom: 15,
        });

        let latLng = {lat: lat, lng: lng};

        let marker = new google.maps.Marker({
            position: latLng,
            title: name,
            map: map,
        });

        let geocoder = new google.maps.Geocoder; // create new geocoder
        let infowindow = new google.maps.InfoWindow(); // create new infoWindow

        geocoder.geocode(
            { "location": latLng },
            function(results, status) {
                // set content for infowindow
                infowindow.setContent(name);

                // print infowindow in map
                infowindow.open(map, marker);
            }
        );
    }

    function setUpChart(){
        let containerChart = $("#container-chart")
        $.get(
            "/api/getdatafordrawinplace/" + idPlaceGlobal,
            function (response){
                if (response.length > 0){
                    let html = ``
                    // let row = ``

                    for (let i = 1; i <= response.length; i++){
                        if (i % 2 === 1){
                            html += `<div class="row container-chart">
                                    <div class="col-6 chart">
                                        <div>${response[i-1].nameDevice}</div>
                                        <span style="display: none">${response[i-1].idDevice}</span>
                                        <span style="display: none">${response[i-1].realTime}</span>
                                        <span style="display: flex; justify-content: center;">${response[i-1].realTime ? "Realtime" : "No realtime"}</span>
                                        <canvas id="chart${response[i-1].idDevice}" style="width:100%;max-width:100%"></canvas>
                                    </div>`
                        }else{
                            html += `<div class="col-6 chart">
                                        <div>${response[i-1].nameDevice}</div>
                                        <span style="display: none">${response[i-1].idDevice}</span>
                                        <span style="display: none">${response[i-1].realTime}</span>
                                        <span style="display: flex; justify-content: center;">${response[i-1].realTime ? "Realtime" : "No realtime"}</span>
                                        <canvas id="chart${response[i-1].idDevice}" style="width:100%;max-width:100%"></canvas>
                                        </div>
                                    </div>`
                        }
                        if (i === response.length){
                            html += `</div>`
                        }
                    }
                    containerChart.html(html)
                    let chartElement = $(".chart")
                    console.log(chartElement)
                    for(let i = 0; i < chartElement.length; i++){
                        console.log(chartElement.eq(i).children().eq(1).text())
                        let idDevice = chartElement.eq(i).children().eq(1).text()
                        $.get(
                            "/api/gettendata?id=" + idDevice,
                            function (response) {
                                const xLabel = []
                                const yLabel = []
                                for (let i = (response.length - 1); i >= 0 ; i--){
                                    let x = response[i]
                                    console.log(x)
                                    console.log(x.time)
                                    let year = x.time.substring(0, 4)
                                    let month = x.time.substring(5, 7)
                                    let day = x.time.substring(8, 10)
                                    let hours = x.time.substring(11, 13)
                                    let minute = x.time.substring(14, 16)
                                    let second = x.time.substring(17, 19)
                                    console.log(year, month, day, hours, minute, second)
                                    xLabel.push(year + "-" + month +"-" + day+" " + hours + ":" + minute + ":" + second)
                                    yLabel.push(x.value.split("%")[0])
                                }
                                drawChart("chart" + idDevice, xLabel, yLabel)
                            }
                        )
                    }
                }else {
                    containerChart.html("Không có dữ thiết bị nào")
                    containerChart.css({
                        "display": "flex",
                        "overflow-x": "auto",
                        "align-items": "center"
                    })
                }
            }
        )
    }

    function upDateChart(){
        let chartElement = $(".chart")
        console.log(chartElement)
        for (let i = 0; i < chartElement.length; i++){
            let idDevice = chartElement.eq(i).children().eq(2).text()
            let realTime = chartElement.eq(i).children().eq(3).text()
            if (realTime === "true"){
                $.get(
                    "/api/gettendata?id=" + idDevice,
                    function (response) {
                        const xLabel = []
                        const yLabel = []
                        for (let i = (response.length - 1); i >= 0 ; i--){
                            let x = response[i]
                            console.log(x)
                            console.log(x.time)
                            let year = x.time.substring(0, 4)
                            let month = x.time.substring(5, 7)
                            let day = x.time.substring(8, 10)
                            let hours = x.time.substring(11, 13)
                            let minute = x.time.substring(14, 16)
                            let second = x.time.substring(17, 19)
                            console.log(year, month, day, hours, minute, second)
                            xLabel.push(year + "-" + month +"-" + day+" " + hours + ":" + minute + ":" + second)
                            yLabel.push(x.value.split("%")[0])
                        }
                        drawChart("chart" + idDevice, xLabel, yLabel)
                    }
                )
            }else {

            }
        }

    }

    function drawChart(idChart, xLabel, yData){
        var xValues = ['12:00:05', '12:00:10', '12:00:15', '12:00:20', '12:00:25', '12:00:30', '12:00:30', '12:00:30', '12:00:30', '12:00:30'];
        new Chart(idChart, {
            type: "line",
            data: {
                labels: xLabel,
                datasets: [{
                    data: yData,
                    borderColor: "red",
                    fill: false
                }]
            },
            options: {
                legend: {display: false},
                scales: {
                    yAxes: [{ticks: {min: 0, max:100}}],
                }
            }
        });
    }

})