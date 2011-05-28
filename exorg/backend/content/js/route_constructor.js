var map;
var posCity;
var directionsDisplay;
var directionsService = new google.maps.DirectionsService();
var markersArray = [];
var waypointsOrder = [];


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
		    zoom:8,
		    mapTypeId: google.maps.MapTypeId.ROADMAP,
		    center: posCity
	    }

	    map = new google.maps.Map(document.getElementById("map"), options);
	    directionsDisplay.setMap(map);
        markersArray = [];
        for (i in data){
            var marker = new google.maps.Marker({
                    map: map,
                    position: new google.maps.LatLng(data[i].Lat, data[i].Lng),
                    title: data[i].Name
            });
            markersArray.push(marker);
            show_infowindow(marker, data[i].Name, data[i].Address, data[i].Url);
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

        directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(result);
                waypointsOrder = result.waypoint_order;
            }
	    });
        showMarkers();
    }
    else {
        initialize();
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

    	var request = {
    		origin: start,
	    	destination: end,
		    waypoints: waypts,
		    optimizeWaypoints: false,
		    travelMode: google.maps.DirectionsTravelMode.WALKING
	    };

        directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(result);
                waypointsOrder = result.waypoint_order;
                showMarkers();
            }
	    });
        showMarkers();
    }
    else {
        initialize();

    }
}


function calculate_route_optimize(data, optimize){

    var waypts = [];
	var count = data.length;
    if (count > 0) {
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
		    optimizeWaypoints: optimize,
		    travelMode: google.maps.DirectionsTravelMode.WALKING
	    };

        directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(result);
                waypointsOrder = result.waypoint_order;
                showMarkers();
            }
	    });
    }
    else {
        initialize();

    }

}


function clearMap() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }

  }
}

