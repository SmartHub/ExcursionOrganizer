var map;
var posCity;
var directionsDisplay;
var directionsService = new google.maps.DirectionsService();
var markersArray = [];
var waypointsOrder = [];
var markerIcons = [];


function show_infowindow(marker, name_poi, address, url){
	var contentString = '<div style="color: blue">' + name_poi + ' </div><br/><div style="color: black"> Адрес: ' + address + ' </div><br/> ' + '<div><a href="' + url + '" >подробнее...</a></div>';
	var infowindow = new google.maps.InfoWindow({
		content: contentString
	}); 
  		
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map,marker);
	}); 
}


function initialize() {
	directionsDisplay = new google.maps.DirectionsRenderer();
	posCity = new google.maps.LatLng(59.63380760, 29.42479870);
	var options = {
		zoom:8,
		mapTypeId: google.maps.MapTypeId.ROADMAP,
		center: posCity
	}
	
	map = new google.maps.Map(document.getElementById("map"), options);
	directionsDisplay.setMap(map);
}

function initialize_full(data){
    if (data.length == 0) {
        initialize();
    }
    else {
        directionsDisplay = new google.maps.DirectionsRenderer();

        directionsDisplay.setOptions({
                suppressMarkers: true
        });

	    posCity = new google.maps.LatLng(59.63380760, 29.42479870);
	    var options = {
		    zoom:4,
		    mapTypeId: google.maps.MapTypeId.ROADMAP,
		    center: posCity
	    }

	    map = new google.maps.Map(document.getElementById("map"), options);
	    directionsDisplay.setMap(map);
        markersArray = [];
        markerIcons = [];
        for (i in data){
            var marker = new google.maps.Marker({
                    map: map,
                    position: new google.maps.LatLng(data[i].Lat, data[i].Lng),
                    title: data[i].Name,
                    icon: image
            });
            markersArray.push(marker);
            show_infowindow(marker, data[i].Name, data[i].Address, data[i].Url);
            var image = new google.maps.MarkerImage('http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld='+i+'|F31121|000000',
                      new google.maps.Size(20, 34),
                      new google.maps.Point(0, 0),
                      new google.maps.Point(10, 34)
            );
            markerIcons.push(image);
        }

    }
}

function showMarkers() {
    for (i in markersArray){
        markersArray[i].setMap(map);
    }
}

function calculate_route(data){

    var waypts = [];
	var count = data.length;
    if (count > 0) {
        if ((markersArray.length == 0) || (data.length != markersArray.length)) {
            initialize_full(data);
        }
        for (var i = 1; i < count - 1; i++) {
		    waypts.push({
			    location:new google.maps.LatLng(data[i].Lat, data[i].Lng),
			    stopover:true
		    });
	    }

        var start = new google.maps.LatLng(data[0].Lat, data[0].Lng);
        var end = new google.maps.LatLng(data[count-1].Lat, data[count-1].Lng);

        var request = {
            origin: start,
	        destination: end,
		    waypoints: waypts,
		    optimizeWaypoints: true,
		    travelMode: google.maps.DirectionsTravelMode.WALKING
	    };
        showDefaultIcons();
        directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {

                if (data.length == 1) {
                    var currentPos = result.routes[0].legs[0].start_location;
                    markersArray[0].setPosition(currentPos);
                    map.setZoom(4);
                }
                directionsDisplay.setDirections(result);
            }
	    });
        showMarkers();


    }
    else {
        initialize();
    }

}

function showDefaultIcons()
{
    for (i in markersArray) {
        markersArray[i].setIcon(null);
    }
}

function calculate_route_unoptimal(data){

    var waypts = [];
	var count = data.length;
    if (count > 0) {
        if ((markersArray.length == 0) || (data.length != markersArray.length)) {
            initialize_full(data);
        }
	    for (var i = 1; i < count - 1; i++) {
		    waypts.push({
			    location:new google.maps.LatLng(data[i].Lat, data[i].Lng),
			    stopover:true
		    });
	    }

    	var start = new google.maps.LatLng(data[0].Lat, data[0].Lng);
	    var end = new google.maps.LatLng(data[count-1].Lat, data[count-1].Lng);
        waypointsOrder = waypts;
    	var request = {
    		origin: start,
	    	destination: end,
		    waypoints: waypts,
		    optimizeWaypoints: false,
		    travelMode: google.maps.DirectionsTravelMode.WALKING
	    };

        directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setOptions({
                    suppressMarkers: true
                });
                directionsDisplay.setDirections(result);
            }
	    });
        for (i in markersArray) {
            var pos = markersArray[i].getPosition();
            switch (pos.toString()) {
                case start.toString():
                    markersArray[i].setIcon(markerIcons[0]);
                    break
                case end.toString():
                    markersArray[i].setIcon(markerIcons[count-1]);
                    break
                default:
                    markersArray[i].setIcon(markerIcons[findIcon(pos)]);
                    break
            }

        }


        showMarkers();
        if (data.length == 1) {
            map.setCenter(markersArray[0].getPosition());
        }
    }
    else {
        initialize();

    }
}

function findIcon(pos) {
    for (j in waypointsOrder) {
        if (waypointsOrder[j].location.toString() == pos.toString()) {
            var index = parseInt(1, 10) + parseInt(j, 10);
            return index;
        }
    }
}


function clearMap() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }

  }
}

