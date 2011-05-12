function init_inner_frame()
{
	updateCheckBoxes();
	alert("init_inner_frame");
}

function add_poi(id, checked)
{
	var action = '';
	if(checked == false)
		action = '&action="delete"';
    else
        action = '&action="add"';
    $.get('constructor.html?poi_id=' + id + '&_ox' + action, {}, {},'xml');

    var PoiList = [];
	$.get('constructor.html?_ox', {}, function(xml)
	{
        $(xml).find('route_point').each(function()
		{
			Poi =
			{
				Id : $(this).attr("poi-id"),
				Name : $(this).find('name').text(),
				Lat : $(this).attr("lat"),
				Lng : $(this).attr("lng"),
				Url : "poi.html?id=" + $(this).attr("poi-id")
			};
			//Poi.Url = "poi.html?id=" + Poi.Id;
            alert(Poi.Id + "; " + Poi.Name + " ; " + Poi.Lat + "; " + Poi.Lng);
			PoiList.push(Poi);
        	});
		alert(PoiList.length & " items draw");
		calculate_route(PoiList);
		/* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */

	}, 'xml'); // указываем явно тип данных
};


function updateCheckBoxes()
{
	for(var i = 0; i < PoiList.length; ++i)
	{
		/* HACK */
		var cb = top.frames["inner-frame"].document.getElementById(PoiList[i].Id);
		if (cb != undefined)
		{
			cb.checked = true;
		}
	}
}

/*
function printPoiList(List)
{
	$('#poi-print').html('');
	$('#poi-print').append('<br/>Выбранные для посещения POI:<br/><hr/>');
	for(var i = 0; i < List.length; ++i)
	{
		$('#poi-print').append(List[i].Name + '<br/><hr/>');
	};
} */
