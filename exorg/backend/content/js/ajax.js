
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
        	});
        updateButtons(PoiList);
        clearMap();
        if (PoiList.length >= 0)
        {
            calculate_route(PoiList);
        }
        printPoiList(PoiList);
	}, 'xml'); // указываем явно тип данных
}

function add_poi(id, caption)
{
    if (not_add == false)
    {
        var action = '';
        if(caption == 'visit') {
            action = '&action="add"';
            if (element(id) != undefined)
		    {
                element(id).value = 'remove';
                //element(id).innerHtml = '<img src="img/icons/delete.png"> Удалить';
				//element(id).title = 'Удалить из списка для посещения';
				element(id).innerText = 'Удалить';
            }
        }
        else {
            action = '&action="delete"';
            if (element(id) != undefined)
		    {
                element(id).value = 'visit';
                element(id).innerText = 'Хочу посетить!';
            }
        }
        //$.get('constructor.html?poi_id=' + id + '&_ox' + action, {}, {},'xml');

        loadPoiList('&poi_id=' + id + action);
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
            //cb.innerHtml = '<img src="img/icons/delete.png"> Удалить';
			//cb.title = 'Удалить из списка для посещения';
			cb.innerText = 'Удалить';
		}
	}
	not_add = false;
}


function printPoiList(List)
{
	$('#poi-print').html('');
	$('#poi-print').append('<br/>  Выбранные для посещения достопримечательности:<br/><hr/>');
	for(var i = 0; i < List.length; ++i)
	{
        $('#poi-print').append("<button type='button' value='remove' onClick='add_poi(" + List[i].Id + ", this.value);' ><img src='img/icons/delete.png'></button>");
		$('#poi-print').append("<a href='poi.html?id=" + List[i].Id + "'>" + List[i].Name + '</a>');
		$('#poi-print').append('<hr/>');
	};
}
