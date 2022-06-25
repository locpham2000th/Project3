
function choosePlace(element) {
    console.error(element)
}

$(document).ready(function(){

    let idUser = $("#id_user").text();
    console.log(idUser);
    $.get(
        // "/place/getlist/" + idUser,
        "/api/getinfo",
        function(response){
            let containPlace = $("#list-place")
            if (response.length > 0){
                // let bodyTable = ``
                // for (let i = 0; i < response.length; i++){
                //     let row = ``
                //     let col = ``
                //     let name = response[i].nameDevice
                //     let value = response[i].value
                //     let ids = response[i].ids
                //     let realTime = response[i].realTimes
                //     col += `<td>${response[i].namePlace}</td>`
                //     for (let j = 0; j < response[i].nameDevice.length; j++){
                //         col += `<td><span>${name[j]}</span> : <span class="id-device" style="display: none">${ids[j]}</span><span style="display: none">${realTime[j]}</span><span>${value[j]}</span></td>`
                //     }
                //     row = `<tr  style="background-color: red" onclick="choosePlace(this)">${col}</tr>`
                //     bodyTable += row
                // }
                // let table = `<table class="table table-place"><tbody>${bodyTable}</tbody></table>`
                // containPlace.html(table)


                let row = ``
                for (let i = 0; i < response.length; i++){
                    let col = ``
                    let name = response[i].nameDevice
                    let value = response[i].value
                    let ids = response[i].ids
                    let realTime = response[i].realTimes
                    col += `<span>${response[i].namePlace}</span>`
                    for (let j = 0; j < response[i].nameDevice.length; j++){
                        col += `<span>${name[j]} :</span><span>${ids[j]}</span><span>${realTime[j]}</span>`
                    }
                    row += `<li>${col}</li>`
                }
                let html = `<ul style="list-style-type:none;">${row}</ul>`
                // containPlace.html(html)
                containPlace.css("flex-grow", "1")
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
            "/logout?id=" + idUser,
            function (){
                window.location = `/`
            }
        )
    })

})
