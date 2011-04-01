var map;
var directionsDisplay;
var directionsService = new google.maps.DirectionsService();


function show_infowindow(marker, name_poi, address, url){
	var contentString = '<div style="color: black;">' + name_poi + ' Адрес: ' + address + '<a href="' + url + '" >подробнее...</a></div>';
	var infowindow = new google.maps.InfoWindow({
		content: contentString
	}); 
  		
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map,marker);
	}); 
}


function initialize() {
	directionsDisplay = new google.maps.DirectionsRenderer();
	var posCity = new google.maps.LatLng(59.63380760, 29.42479870);
	var options = {
		zoom:8,
		mapTypeId: google.maps.MapTypeId.ROADMAP,
		center: posCity
	}
	
	map = new google.maps.Map(document.getElementById("map"), options);
	directionsDisplay.setMap(map);
}

function calculate_route(data){

	var waypts = [];
	var count = data.length;
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
		}
	});
}


