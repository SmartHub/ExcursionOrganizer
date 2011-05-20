var PoiList = [];
var length = 0;

$(document).ready(
	function(){
		//$("input.number").bind('change', check);
		loadPoiList();
		calculate_route(PoiList);
		//showPoiList(PoiList);
		//showPoiList(PoiList);
	}
);


function loadPoiList() {
    PoiList.length = 0;
	$.get('route.html?_ox', {}, function(xml)
	{
        $(xml).find('page').find('data').find('route_point').each(function() ///data/route_point
		{
			var Poi ={
				Id : $(this).attr("poi-id"),
				Order : $(this).attr("order"),
				Name : $(this).find('name').text(),
				Addr : $(this).find('address').text(),
				Lat : $(this).attr("lat"),
				Lng : $(this).attr("lng"),
				Url : "poi.html?id=" + $(this).attr("poi-id")
			};
			PoiList.push(Poi);
			//alert(Poi.Name);
        	});
        //alert("update fin");
        showPoiList(PoiList);
        calculate_route(PoiList);
        length = PoiList.length;
	}, 'xml'); // указываем явно тип данных
}


function showPoiList(List) {
	$('#poi-print').html('');

    s = "<table cellpadding = '5'>";
	for(var i = 0; i < List.length; ++i)
	{
	    s += '<tr><td>';

        s +="<input type='button' class='up' value = '^' id='" + List[i].Id + "u' onClick='up_poi(" +
		    List[i].Id +
		    ");' ></input>";
		s += "<td/><td>";
		s +="<input type='button' value = 'V' id='" + List[i].Id + "d' onClick='down_poi(" +
		    List[i].Id +
		    ");' ></input>";

		s += "<td/><td><a href='poi.html?id=" + List[i].Id + "'>" + List[i].Name + '</a>';
		s += "<td/><td>";
		s += List[i].Addr;
		s += "<td>";

        s += '<td/><tr/>';
	};

	s += '<table/>';
	s += "<input type='button' value = 'Сохранить изменения' onClick='save_order();' id='666'></input>";

	$('#poi-print').append(s);

    $('#' + PoiList[0].Id + 'u').attr('disabled', true);
    $('#' + PoiList[PoiList.length - 1].Id + 'd').attr('disabled', true);
}

function swap_poi(i, j) {

    Poi = PoiList[i];
    PoiList[i] = PoiList[j];
    PoiList[j] = Poi;
    Order = PoiList[i].Order;
    PoiList[i].Order = PoiList[j].Order;
    PoiList[j].Order = Order;
}

function up_poi(id) {
    //PoiList.length = length;
    for (i = 1; i < PoiList.length; ++i) {
        if (PoiList[i].Id == id)
        {
            swap_poi(i, i-1);
            showPoiList(PoiList);
            calculate_route(PoiList);
            return;
        }
    }
}

function down_poi(id) {
    //PoiList.length = length;
    for (i = 0; i < PoiList.length-1; ++i) {
        if (PoiList[i].Id == id)
        {
            swap_poi(i, i+1);

            showPoiList(PoiList);
            calculate_route(PoiList);
            return;
        }
    }
}

function save_order()
{
    s = "&change_order";
    for (i = 0; i < PoiList.length; ++i) {
        s += "&" + PoiList[i].Id + "=" + PoiList[i].Order;
    }
    	//$.get('constructor.html?_ox' + s, {}, {}, 'xml');
	$.get('constructor.html?_ox' + s, {}, function(xml){}, 'xml'); // указываем явно тип данных
}




