var not_add = false;

function init_inner_frame()
{
	loadPoiList("");
	//alert();
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
        updateCheckBoxes(PoiList);
        if (PoiList.length != 0)
        {
            calculate_route(PoiList);
        }
        printPoiList(PoiList);
        //alert("update fin");
	}, 'xml'); // указываем явно тип данных
}

function add_poi(id, checked)
{
    if (not_add == false)
    {
        var action = '';
        if(checked == false){
            action = '&action="delete"';
        }
        else{
            action = '&action="add"';
        }
        //$.get('constructor.html?poi_id=' + id + '&_ox' + action, {}, {},'xml');

        loadPoiList('&poi_id=' + id + action);
            /* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */
	}
};


function updateCheckBoxes(List)
{
    not_add = true;
    $(top.frames["inner-frame"].document).find('.cb').each(function(){
        $(this).attr('checked', false);
    });


	for(var i = 0; i < List.length; ++i)
	{
		/* HACK */
		var cb = top.frames["inner-frame"].document.getElementById(List[i].Id);
		if (cb != undefined)
		{
			cb.checked = true;
		}
	}
	not_add = false;
}


function printPoiList(List)
{

    //alert("printPoiList()" + List.length);
	$('#poi-print').html('');
	$('#poi-print').append('<br/>Выбранные для посещения POI:<br/><hr/>');
	for(var i = 0; i < List.length; ++i)
	{
		$('#poi-print').append("<a href='poi.html?id=" + List[i].Id + "'>" + List[i].Name + '</a><br/>');
		$('#poi-print').append(
		    "<br/><input type='button' value = 'удалить' onClick='add_poi(" +
		    List[i].Id +
		    ", false);' ></input>");
		$('#poi-print').append('<hr/>');
	};


	//$('#poi-print').load('constructor.html?_ox');
}