var map;
var posCity;
var directionsDisplay;
var directionsService = new google.maps.DirectionsService();
var markersArray = [];


function show_infowindow(marker, name_poi, address, url){
	var contentString = '<div style="color: blue">' + name_poi + ' </div><br/>'; //<div style="color: black"> Адрес: ' + address + ' </div><br/> ' + '<div><a href="' + url + '" >подробнее...</a></div>';
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

function calculate_route(data){

    clearMap();
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
		    optimizeWaypoints: true,
		    travelMode: google.maps.DirectionsTravelMode.WALKING
	    };

        directionsService.route(request, function(result, status) {
		    if (status == google.maps.DirectionsStatus.OK) {
			    directionsDisplay.setDirections(result);
                var myRoute = result.routes[0].legs[0];
                for (var i = 0; i < myRoute.steps.length; i++) {
                    var marker = new google.maps.Marker({
                        position: myRoute.steps[i].start_point,
                        map: map
                    });
                    show_infowindow(marker, myRoute.steps[i].instructions, "", "");
                    markersArray[i] = marker;
                }
		    }
	    });
    }
    else {
        map.setCenter(posCity);
    }
}

function clearMap() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
    markersArray = [];
  }

}

