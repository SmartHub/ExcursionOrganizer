		
		var directionDisplay;
var directionsService = new google.maps.DirectionsService();
var map;

function initialize() {
  directionsDisplay = new google.maps.DirectionsRenderer();
  var chicago = new google.maps.LatLng(58.850033, 31.6500523);
  var myOptions = {
    zoom:7,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    center: chicago
  }
  map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
  directionsDisplay.setMap(map);
}
  
function calcRoute() {
	
  var start = document.getElementById("start").value;
  var stopov = document.getElementById("stopover").value;
  var end = document.getElementById("end").value;
  var request = {
    origin:start, 
    destination:end,
    travelMode: google.maps.DirectionsTravelMode.WALKING,
    waypoints: [
		{
      location:stopov, 
      stopover:true
    }    
    ],
    optimizeWaypoints: true,
    provideRouteAlternatives: false,
    avoidHighways: true
  };
  directionsService.route(request, function(result, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      
      directionsDisplay.setDirections(result);
    }
  });
}

