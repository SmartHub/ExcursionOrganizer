var map;
var nearestMarkersArray = [];
var mainMarker;
var PoiList;
//var testMarker;


function show_infowindow(marker, name_poi, address, url){
  var contentString = '<div style="color: blue">' + name_poi + ' </div><br/><div style="color: black"> Адрес: ' + address + ' </div><br/> ' + '<div><a href="' + url + '" >подробнее...</a></div>';
  var infowindow = new google.maps.InfoWindow({
  	content: contentString
  }); 
  		
  google.maps.event.addListener(marker, 'click', function() {
  	infowindow.open(map, marker);
  }); 
}

 function initialize(lat, long, name_poi, address, url) {
  
  var posLatlng = new google.maps.LatLng(lat, long, name_poi);
    var options = {
        zoom:16,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: posLatlng
     };
	
     map = new google.maps.Map(document.getElementById("map"), options);
     mainMarker = new google.maps.Marker({
		position: posLatlng,
		map: map,
		title: name_poi
	 });

	 show_infowindow(mainMarker, name_poi, address, url);
}

function initializeFull(poiId, lat, long, name_poi, address, url) {

    var posLatlng = new google.maps.LatLng(lat, long, name_poi);
    var options = {
        zoom:16,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: posLatlng
     };

    map = new google.maps.Map(document.getElementById("map"), options);

    mainMarker = new google.maps.Marker({
		position: posLatlng,
		map: map,
		title: name_poi
	});
    show_infowindow(mainMarker, name_poi, address, url);

    nearestMarkersArray = [];
    PoiList = [];
    var i = 0;
    $.get('poi.html?id='+poiId+'&_ox', {}, function(xml)
    {
        $(xml).find('nearest_poi').each(
        function() {
            var Poi =
            {
                Id : $(this).attr("id"),
                Name : $(this).find('name').text(),
                Address : $(this).find('address').text(),
                Lat : $(this).attr("lat"),
                Lng : $(this).attr("lng"),
                Url : "poi.html?id=" + $(this).attr("id")
            };
            PoiList[i] = Poi;
            i = i + 1;
        });

        if (PoiList.length == 0)
        {
            alert ("No places of interest found close to this!");
        }
        else
        {
            //alert(PoiList.length);
            for (j in PoiList){
                var testMarker = new google.maps.Marker({
		            position: new google.maps.LatLng(PoiList[j].Lat, PoiList[j].Lng),
                    map: map,
		            title: "test"
                });
                show_infowindow(testMarker, PoiList[j].Name, PoiList[j].Address, PoiList[j].Url);
                nearestMarkersArray[j] = testMarker;
            }
            hidePois();
        }
    }, 'xml');

}

function showPois () {
    if (nearestMarkersArray)
    {
        for (i in nearestMarkersArray)
        {
            nearestMarkersArray[i].setMap(map);
        }
    }


}

function hidePois()
{
    if (nearestMarkersArray)
    {
        for (i in nearestMarkersArray)
        {
            nearestMarkersArray[i].setMap(null);
        }
    }
}

function showNearestPoi(poiId, checked) {
    if (checked) {
        showPois();
    }
    else {
        hidePois();
    }
}

var pois_shown = false;
function showNearestPoiButton() {
    var enter = String.fromCharCode(13) + String.fromCharCode(10);
    if (pois_shown) {
        pois_shown = false;
        hidePois();
        $("#poi_button").attr("value", "Показать" +  enter + "достопримечательности" + enter + "рядом");
    } else {
        pois_shown = true;
        showPois();
        $("#poi_button").attr("value", "Скрыть" + enter + "достопримечательности");    
    }
}


