var sid='';

$(document).ready(function(){                         // по завершению загрузки страницы
    $('#checkbox').click(function(){                  // вешаем на клик по элементу с id = example-3
        $.get('test-poi.html?id=6&_ox', {}, function(xml){  // загрузку XML из файла example.xml
            $('#checkbox').html('');
            $(xml).find('poi').each(function(){       // заполняем DOM элемент данными из XML

		initialize($(this).find('lat').text(), $(this).find('lng').text(), '123')
                //$('#checkbox').append('ID: '   + $(this).find('id').text() + '<br/>')
                //               .append('Name ' + $(this).find('name').text() + '<br/>');
            });
        }, 'xml');                                     // указываем явно тип данных
    })
});

function ajax_debug(msg){
	alert("debug " + msg);
};

function show_on_map(id)
{
	$.get('test-poi.html?id=' + id + '&_ox', {}, function(xml)
	{  // загрузку XML из файла example.xml
		$('#checkbox').html('');
		$(xml).find('poi').each(function(){       // заполняем DOM элемент данными из XML

		initialize($(this).find('lat').text(), $(this).find('lng').text(), '123')
                //$('#checkbox').append('ID: '   + $(this).find('id').text() + '<br/>')
                //               .append('Name ' + $(this).find('name').text() + '<br/>');
            });
        }, 'xml');                                     // указываем явно тип данных
};


function add_poi(id)
{
	PoiList = [];

	$.get('constructor.html?poi_id=' + id + '&_ox&sid=' + sid, {}, function(xml)
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


function printPoiList(List)
{
	$('#poi-print').html('');
	for(var i = 0; i < List.length; ++i)
	{
		$('#poi-print').append(
			'ID = ' + List[i].Id + 
			'<br/> Name: ' + List[i].Name + 
			'<br/> Lat: ' + List[i].Lat  + 
			'<br/> Lng: ' + List[i].Lng
			 + '<br/><br/>');
	};
}
