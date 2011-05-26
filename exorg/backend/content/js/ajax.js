
var not_add = false;

function init_inner_frame()
{
	loadPoiList("");
	//alert();
}

function element(id) {
    return top.frames["inner-frame"].document.getElementById(id);
}

function loadPoiList(Action)
{
    var PoiList = [];
	$.get('constructor.html?_ox' + Action, {}, function(xml)
	{
        $(xml).find('page').find('data').find('route_point').each(function() ///data/route_point
		{
			var Poi ={
				Id : $(this).attr("poi-id"),
				Name : $(this).find('name').text(),
				Lat : $(this).attr("lat"),
				Lng : $(this).attr("lng"),
				Url : "poi.html?id=" + $(this).attr("poi-id")
			};
			PoiList.push(Poi);
			//alert(Poi.Name);
        	});
        //alert("update");
        updateButtons(PoiList);
        clearMap();
        //if (PoiList.length != 0)
        if (PoiList.length >= 0)
        {
            calculate_route(PoiList);
        }
        printPoiList(PoiList);
        //alert("update fin");
	}, 'xml'); // указываем явно тип данных
}

function add_poi(id, caption)
{
    if (not_add == false)
    {
        var action = '';
        if(caption == 'visit') {
            action = '&action="add"';
            element(id).value = 'remove';
            element(id).innerText = 'Удалить';
        }
        else {
            action = '&action="delete"';
            element(id).value = 'visit';
            element(id).innerText = 'Хочу посетить!';
        }
        //$.get('constructor.html?poi_id=' + id + '&_ox' + action, {}, {},'xml');

        loadPoiList('&poi_id=' + id + action);
            /* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */
	}
};


function updateButtons(List)
{
    not_add = true;
    $(top.frames["inner-frame"].document).find('.cb').each(function() {
        $(this).attr('value', 'visit');
        $(this).attr('innerText', "Хочу посетить!");
    });

	for(var i = 0; i < List.length; ++i)
	{
		/* HACK */
		var cb = element(List[i].Id);
		if (cb != undefined)
		{
            cb.value = 'remove';
            cb.innerText = "Удалить";
		}
	}
	not_add = false;
}


function printPoiList(List)
{

    //alert("printPoiList()" + List.length);
	$('#poi-print').html('');
	$('#poi-print').append('<br/>Выбранные для посещения достопримечательности:<br/><hr/>');
	for(var i = 0; i < List.length; ++i)
	{
		$('#poi-print').append("<a href='poi.html?id=" + List[i].Id + "'>" + List[i].Name + '</a><br/>');
		$('#poi-print').append(
		    "<br/><input type='button' value='remove' onClick='add_poi(" +
		    List[i].Id +
		    ", false);' ></input>");
		$('#poi-print').append('<hr/>');
	};


	//$('#poi-print').load('constructor.html?_ox');
}