//var sid='';

var PoiList = [];

function init_inner_frame()
{
	updateCheckBoxes();
}

function add_poi(id, checked)
{
	PoiList = [];

	var do_delete = '';
	if(checked == false)
	{
		do_delete = '&action="delete"';
	}
	$.get('constructor.html?poi_id=' + id + '&_ox&sid=' + sid + do_delete, {}, function(xml)
	{
        	$(xml).find('route').find('pois').find('poi').each(function()
		{
			Poi =
			{
				Id : $(this).find('id').text(),
				Name : $(this).find('name').text(),
				Lat : $(this).find('lat').text(),
				Lng : $(this).find('lng').text()
			};
			PoiList.push(Poi);
        	});

		printPoiList(PoiList);
		
		calculate_route(PoiList);
		/* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */

		sid = $(xml).find('route').find('sid').text();
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


function printPoiList(List)
{
	$('#poi-print').html('');
	$('#poi-print').append('<br/>Выбранные для посещения места:<br/><hr/>');
	for(var i = 0; i < List.length; ++i)
	{
		$('#poi-print').append(List[i].Name + '<br/><hr/>');
	};
}
