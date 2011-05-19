var map;
var nearestMarkersArray = [];
var mainMarker;
var testMarker;


function show_infowindow(marker, name_poi, address, url){
  var contentString = '<div style="color: black;">' + name_poi + ' Адрес: ' + address + '<a href="' + url + '" >подробнее...</a></div>';
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

    //createNearestMarkersArray(poiId);

    nearestMarkersArray = [];
    var PoiList = [];
    $.get('poi.html?id='+poiId+'&_ox', {}, function(xml)
    {
        $(xml).find('nearest_poi').each(
        function() {
            Poi =
            {
                Id : $(this).attr("id"),
                Name : $(this).find('name').text(),
                Address : $(this).find('address').text(),
                Lat : $(this).attr("lat"),
                Lng : $(this).attr("lng"),
                Url : "poi.html?id=" + $(this).attr("id")
            };
            //PoiList.push(Poi);
            var nearestMarker =  new google.maps.Marker ({
                    location: new google.maps.LatLng(Poi.Lat, Poi.Lng),
                    map: map,
                    title: Poi.Name
            });
            nearestMarkersArray.push(nearestMarker);
            show_infowindow(nearestMarker, Poi.Name, Poi.Address, Poi.Url);
        });

        if (nearestMarkersArray.length == 0)
        {
            alert ("No places of interest found close to this!");
        }
        else
        {
            alert(nearestMarkersArray.length);
            /*
            nearestMarkersArray = [];
            for (var i = 1; i < PoiList.length - 1; i++) {
                var nearestMarker =  new google.maps.Marker ({
                    location: new google.maps.LatLng(PoiList[i].Lat, PoiList[i].Lng),
                    map: map,
                    title: PoiList[i].Name
                });
                nearestMarkersArray.push(nearestMarker);
                show_infowindow(nearestMarker, PoiList[i].Name, PoiList[i].Address, PoiList[i].Url);
            }
            */
        }
    }, 'xml');


     /*
        testMarker = new google.maps.Marker({

         location: new google.maps.LatLng(PoiList[1].Lat, PoiList[1].Lng),
         map: map,
         title: PoiList[1].Name
     });
     show_infowindow(testMarker, PoiList[1].Name, PoiList[1].Address, PoiList[i].Url);
      */




   // hidePois();
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



function createNearestMarkersArray(poiId)
{
    /*var PoiList = [];
    $.get('poi.html?id='+poiId+'&_ox', {}, function(xml)
    {
        $(xml).find('nearest_poi').each(
        function() {
            Poi =
            {
                Id : $(this).attr("id"),
                Name : $(this).find('name').text(),
                Address : $(this).find('address').text(),
                Lat : $(this).attr("lat"),
                Lng : $(this).attr("lng"),
                Url : "poi.html?id=" + $(this).attr("id")
            };
            PoiList.push(Poi);
        });
        if (PoiList.length == 0)
        {
            alert ("No places of interest found close to this!");
        }
        else
        {
            alert(PoiList.length);
            nearestMarkersArray = [];
            for (var i = 1; i < PoiList.length - 1; i++) {
                var nearestMarker =  new google.maps.Marker ({
                    location: new google.maps.LatLng(PoiList[i].Lat, PoiList[i].Lng),
                    map: map,
                    title: PoiList[i].Name
                });
                nearestMarkersArray.push(nearestMarker);
                show_infowindow(nearestMarker, PoiList[i].Name, PoiList[i].Address, PoiList[i].Url);
            }
        }
    }, 'xml');

    */

}

function showNearestPoi(poiId, checked) {
    if (checked) {
        /*var PoiList = [];
        $.get('poi.html?id='+poiId+'&_ox', {}, function(xml)
        {
            $(xml).find('nearest_poi').each(
                function() {
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
                createNearestMarkersArray(PoiList);
                showPois();
            }
        }, 'xml');
        */
        showPois();
    }
    else {
        //alert("hide");
        hidePois();
    }
}


