
function choosePlace(element) {
    console.error(element)
    let place = $(element)
    // console.error(place)
    console.error(place.eq(0).children(".list-place-row-name").eq(0).children(".list-place-row-idPlace").text())
    let idPlace = place.eq(0).children(".list-place-row-name").eq(0).children(".list-place-row-idPlace").text()

    window.location = `/place/${idPlace}`


}

// setTimeout(sendName, 5000)

// function sendName() {
//     stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
// }

// var connection = new WebSocket('ws://localhost:8080/mywebsocket/topic/news');
//
// connection.onopen = function(){
//     console.error('Kết nối thành công');
// }
//
// connection.onmessage = function(e) {
//     console.error('data response', e.data);
// }

function addPlace() {
    console.error("hello")
}

$(document).ready(function(){

    let idUser = $("#id_user").text();

    let stompClient = null;

    connect()
    function connect() {
        var socket = new SockJS('/mywebsocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.error('Connected: ' + frame);
            console.error("/" + idUser + "/topic/greetings")
            stompClient.subscribe("/user/" + idUser +'/topic/greetings', function (greeting) {
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

    setTimeout(sendName, 5000)

    function sendName() {
        console.error("đã send từ client lên server")
        stompClient.send("/app/hello", {}, JSON.stringify({'name': "loc"}));
    }

    function editAlarm(message) {
        message = JSON.parse(message)
        // console.error(message)
        let bodyAlarm = $(".body-alarm")
        let bodyTableAlarm = bodyAlarm.eq(0).find("tbody")
        let namePlace = message.namePlace
        let nameDevice = message.nameDevice
        let title = message.title
        // console.error(namePlace, nameDevice, title)
        // console.error(bodyTableAlarm)
        let tr = `<tr><td>${namePlace}</td><td>${nameDevice}</td><td>${title}</td></tr>`
        bodyTableAlarm.prepend(tr)
    }


    console.log(idUser);
    $.get(
        // "/place/getlist/" + idUser,
        "/api/getinfo",
        function(response){
            console.error(response)
            let containPlace = $("#list-place")
            if (response.length > 0){
                let bodyPlace = ``
                for (let i = 0; i < response.length; i++){
                    let row = ``
                    let col = ``
                    let name = response[i].nameDevice
                    let values = response[i].value
                    let ids = response[i].ids
                    let realTime = response[i].realTimes
                    let actives = response[i].active
                    console.error(actives)
                    col += `<div class="list-place-row-name">
                                ${response[i].namePlace}
                                <span style="display: none" class="list-place-row-idPlace">${response[i].idPlace}</span>
                            </div>`
                    for (let j = 0; j < response[i].nameDevice.length; j++){
                        let value = actives[j] === true ? values[j] : "-"
                        console.error(value)
                        // row += `<td><span>${name[j]}</span> : <span class="id-device" style="display: none">${ids[j]}</span><span style="display: none">${realTime[j]}</span><span>${value[j]}</span></td>`
                        row += `<div class="list-place-row-col-device" >
                                    <span>${name[j]} : </span>
                                    <span class="id-device" style="display: none">${ids[j]}</span>
                                    <span style="display: none">${realTime[j]}</span>
                                    <span>${value}</span>
                                    <span style="display: none">${actives[j]}</span>
<!--                                    <span>${value[j]}</span>-->
                                </div>`
                    }
                    if (response[i].nameDevice.length <= 0){
                        row = "Không có thiết bị ở đây"
                        containPlace.css("style", "display: flex; justify-content: center;")
                    }
                    row = `<div class="list-place-row">
                                <div onclick="choosePlace(this)" class="list-place-row-contain">
                                    ${col}
                                    <div class="list-place-row-col">
                                        ${row}
                                    </div>
                                </div>
                                <div class="button-delete-place" onclick="deletePlace(this)">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--! Font Awesome Pro 6.1.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2022 Fonticons, Inc. --><path d="M576 384C576 419.3 547.3 448 512 448H205.3C188.3 448 172 441.3 160 429.3L9.372 278.6C3.371 272.6 0 264.5 0 256C0 247.5 3.372 239.4 9.372 233.4L160 82.75C172 70.74 188.3 64 205.3 64H512C547.3 64 576 92.65 576 128V384zM271 208.1L318.1 256L271 303C261.7 312.4 261.7 327.6 271 336.1C280.4 346.3 295.6 346.3 304.1 336.1L352 289.9L399 336.1C408.4 346.3 423.6 346.3 432.1 336.1C442.3 327.6 442.3 312.4 432.1 303L385.9 256L432.1 208.1C442.3 199.6 442.3 184.4 432.1 175C423.6 165.7 408.4 165.7 399 175L352 222.1L304.1 175C295.6 165.7 280.4 165.7 271 175C261.7 184.4 261.7 199.6 271 208.1V208.1z"/></svg>
                                </div>
                        </div>`
                    // row = `<tr style="background-color: red" onclick="choosePlace(this)">${col}</tr>`
                    bodyPlace += row
                }
                // let table = `<table class="table table-place"><tbody>${bodyTable}</tbody></table>`
                containPlace.html(bodyPlace)


                // let row = ``
                // for (let i = 0; i < response.length; i++){
                //     let col = ``
                //     let name = response[i].nameDevice
                //     let value = response[i].value
                //     let ids = response[i].ids
                //     let realTime = response[i].realTimes
                // }
                // containPlace.html(row)
                // containPlace.css("flex-grow", "1")
                setInterval(gethistoryinplace, 10000)
                // gethistoryinplace()
                //ve cac bieu do
                setUpChart();
                setInterval(upDateChart, 10000);
            }else {
                containPlace.html("Không có địa điểm nào")
            }
        })

    function gethistoryinplace() {
        let now = new Date()
        let minutes = now.getMinutes()
        let second = now.getSeconds()
        let ids = $(".id-device")
        for (let i = 0; i < ids.length; i++){
            let id = ids.eq(i).text()
            let realTime = ids.eq(i).next().text()
            let valueElement = ids.eq(i).next().next()
            let value = ids.eq(i).next().next().text()
            let active = ids.eq(i).next().next().next().text()
            // console.error(realTime)
            // console.error(valueElement)
            // console.error(value)
            if (active === "true"){
                if (realTime === "true"){
                    // dung api lay gia tri
                    let url = `/api/gethistorydevice?id=${id}`
                    $.get(
                        url,
                        function (response) {
                            console.log(response.value)
                            valueElement.html(response.value)
                        }
                    )
                }else {
                    if (minutes === 30){
                        if (second < 10){
                            //dung api lay gia tri'
                            let url = `/api/gethistorydevice?id=${id}`
                            $.get(
                                url,
                                function (response) {
                                    console.log(response.value)
                                    valueElement.html(response.value)
                                }
                            )
                        }
                    }
                }
            }else {
                valueElement.html("-")
            }
            console.log(id, realTime , value)
        }
    }



    function setUpChart(){
        let containerChart = $("#container-chart")
        $.get(
            "/api/getdatafordraw",
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
        let idUser = $("#id_user").text()
        $.get(
            "/logout",
            function (){
                window.location = `/`
            }
        )
    })

    $('#exampleModalCenter').on('shown.bs.modal', function () {
        console.error("helloo")
        $('#exampleModalCenter').trigger('focus')
    })

    $.ajax({
        url: `/api/place`,
        type: "GET",
        success: function (response) {
            console.error(response)
            initMap(response)
        }
    })

    function initMap(response) {

        let map;
        // 16.819655944633247, 106.96457970120021
        map = new google.maps.Map(document.getElementById("map"), {
            center: { lat: 16.819655944633247, lng: 106.96457970120021 },
            zoom: 4,
        });

        for(let i = 0; i < response.length; i++){
            let lat = response[i].lat
            let lng = response[i].lng
            let name = response[i].name

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

    }
    // window.initMap = initMap;

})



function confirmAddPlace(element) {
    let formAddPlace = $(".form-control")
    let namePlace = formAddPlace.eq(0).val()
    let latPlace = formAddPlace.eq(1).val()
    let lngPlace = formAddPlace.eq(2).val()
    console.error(namePlace, latPlace, lngPlace)
    $.ajax({
        url : "/api/place",
        type: "POST",
        // dataType: "json",
        // contentType: "application/json",
        data: {
            name : namePlace,
            lat : latPlace,
            lng : lngPlace
        },
        success: function (response) {
            console.error(response)
            alert("Thêm thành công")
            window.location = `/`
        },
        error: function (error) {
            console.error(error)
            alert("Thêm thất bại")
        }
    })
    console.error("hello")
}

function deletePlace(element){
    let idPlace = $(element).parent().children(".list-place-row-contain").eq(0).children(".list-place-row-name").eq(0).children(".list-place-row-idPlace").eq(0).text()
    $.ajax({
        url: `/api/place/${idPlace}`,
        type: "DELETE",
        success: function () {
            alert("Xóa thành công")
            window.location = "/"
        },
        error: function (){
            alert("Xóa thất bại")
            window.location = "/"
        }
    })
}

