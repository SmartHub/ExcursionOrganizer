var directionsDisplay;
var map;
var directionsService = new google.maps.DirectionsService();

function show_infowindow(marker, name_poi, address, url){
	var contentString = '<div id="content" style="color: blue;">' + name_poi + '/br Адрес: ' + address + '/br<a href="' + url + '" >подробнее...</a></div>';
  var infowindow = new google.maps.InfoWindow({
  	content: contentString
  }); 
  		
  google.maps.event.addListener(marker, 'click', function() {
  	infowindow.open(map,marker);
  }); 
}

function initialize(lat, long, name_poi, address, url) {
  directionsDisplay = new google.maps.DirectionsRenderer();
  var posLatlng = new google.maps.LatLng(lat, long, name_poi);
  var options = {
    zoom:7,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    center: posLatlng
  }
	
  map = new google.maps.Map(document.getElementById("map"), options);
	var marker = new google.maps.Marker({
		position: posLatlng,
		map: map,
		title: caption
	});
	show_infowindow(marker, name_poi, address, url);
  directionsDisplay.setMap(map);
	
}

function add_marker(lat, long, name_poi, address, url){
	var pos = new google.maps.LatLng(lat, long, caption);
	var marker = new google.maps.Marker({
		position: pos, 
		map: map,
		title: caption
	});  
	var contentString = '<div id="content" style="color: blue;">' + name_poi + '/br Адрес: ' + address + '/br<a href="' + url + '" >подробнее...</a></div>';
  var infowindow = new google.maps.InfoWindow({
  	content: contentString
  }); 
  		
  google.maps.event.addListener(marker, 'click', function() {
  	infowindow.open(map,marker);
  }); 
}

function calculate_route(){
	
	var waypts = [];
  var checkboxArray = document.getElementById("waypoints");
  for (var i = 0; i < checkboxArray.length; i++) {
    if (checkboxArray.options[i].selected == true) {
      waypts.push({
          location:checkboxArray[i].value,
          stopover:true
      });
    }
  }
	var start = waipts[0].value;
  var end = waipts[waipts.length - 1].value;
	
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
