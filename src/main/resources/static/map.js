var map = L.map('map').setView([48.78, 9.18], 7);

L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: 'pk.eyJ1IjoicHJvZ3JhbW1pZXJwcm9qZWN0IiwiYSI6ImNrejczNHBrODBpOTgycm45djZpczl3dWYifQ.zaOR_HhPpmgLZCjWpBVX_A'
}).addTo(map);

var curQuery = [];
var path;
function makeRequest() {

    if (curQuery.length < 2) {
        alert("Two points are supposed to be selected.")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/minDist",
        contentType: 'application/json',
        processData: false,
        data: JSON.stringify({
            "start": JSON.stringify(curQuery[0].getLatLng()),
            "end": JSON.stringify(curQuery[1].getLatLng())
        })
        ,
        success: function (result) {
            var args = JSON.parse(result);
            args["coordinates"] = JSON.parse(args["coordinates"]);
            var nearestStartPoint = args["coordinates"][0];
            var nearestEndPoint = args["coordinates"][args["coordinates"].length - 1];
            curQuery.push(new L.marker(nearestStartPoint).addTo(map));
            curQuery.push(new L.marker(nearestEndPoint).addTo(map));
            document.querySelector("#nearstPoints").innerHTML = "Nearest Start:<br> [" + [nearestStartPoint[0].toFixed(2), nearestStartPoint[1].toFixed(2)] + "]<br>\
            Nearest End:<br> [" + [nearestEndPoint[0].toFixed(2), nearestEndPoint[1].toFixed(2)] + "]";
            // console.log(args);
            path = new L.polyline(args["coordinates"]).addTo(map);
        }
    });
}

document.querySelector("body > div > div > div.col-sm > div > div > div.card-body > button").addEventListener('click', makeRequest);

function onMapClick(e) {
    // create a lable as the start/end point
    if (curQuery.length >= 2) {
        for (var point of curQuery) {
            point.remove();
        }
        path.remove();
        curQuery = [];
        document.querySelector("#startPoint").innerHTML = "Start";
        document.querySelector("#endPoint").innerHTML = "Destination";
        document.querySelector("#nearstPoints").innerHTML = "";
        return;
    }
    var idx = curQuery.length;
    curQuery.push(new L.marker(e.latlng).addTo(map));
    if (idx == 0) {
        document.querySelector("#startPoint").innerHTML = "Start:<br> [" + [(e.latlng.lat).toFixed(2), (e.latlng.lng).toFixed(2)] + "]";
    } else if (idx == 1) {
        document.querySelector("#endPoint").innerHTML = "Destination:<br> [" + [(e.latlng.lat).toFixed(2), (e.latlng.lng).toFixed(2)] + "]";
    }


}

map.on('click', onMapClick);
