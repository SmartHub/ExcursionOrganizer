var map;

function show_infowindow(marker, name_poi, address, url){
	var contentString = '<div style="color: black;">' + name_poi + ' Адрес: ' + address + '<a href="' + url + '" >подробнее...</a></div>';
  var infowindow = new google.maps.InfoWindow({
  	content: contentString
  }); 
  		
  google.maps.event.addListener(marker, 'click', function() {
  	infowindow.open(map,marker);
  }); 
}

 function initialize(lat, long, name_poi, address, url) {
  
  var posLatlng = new google.maps.LatLng(lat, long, name_poi);
  var options = {
    zoom:16,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    center: posLatlng
  }
	
  map = new google.maps.Map(document.getElementById("map"), options);
	var marker = new google.maps.Marker({
		position: posLatlng,
		map: map,
		title: name_poi
	});
	show_infowindow(marker, name_poi, address, url);
}


