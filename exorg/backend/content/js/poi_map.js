var map;
var markersArray = [];

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

function showPois (data) {
	for (var i = 1; i < data.length - 1; i++) {
        var marker = new google.maps.Marker ({
            location: new google.maps.LatLng(data[i].Lat, data[i].Lng),
            map: map,
            title: data[i].Name
        });
        show_infowindow(marker, data[i].Name, data[i].Address, data[i].Url);
	}
}

function showNearestPoi(poiId, checked) {
    if (checked) {
        var PoiList = [];
        $.get('poi.html?id='+poiId+'&_ox', {}, function(xml)
        {
            $(xml).find('nearest_poi').each(function()
            {
                Poi =
                {
                    Id : $(this).attr("id"),
                    Name : $(this).find('name').text(),
                    Address : $(this).find('address').text(),
                    Lat : $(this).attr("lat"),
                    Lng : $(this).attr("lng"),
                    Url : "poi.html?id=" + $(this).attr("id")
                };
                //alert(Poi.Id + "; " + Poi.Name + " ; " + Poi.Lat + "; " + Poi.Lng);
                PoiList.push(Poi);
                });
            if (PoiList.length == 0)
                alert ("No places of interest found close to this!");
            else {
                alert(PoiList.length);
                showPois(PoiList);
            }
        }, 'xml');
    }
    else {
        //alert("hide");
    }
}


