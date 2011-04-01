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

    function show_on_map(id){                  // вешаем на клик по элементу с id = example-3
        $.get('test-poi.html?id=' + id + '&_ox', {}, function(xml){  // загрузку XML из файла example.xml
            $('#checkbox').html('');
            $(xml).find('poi').each(function(){       // заполняем DOM элемент данными из XML

		initialize($(this).find('lat').text(), $(this).find('lng').text(), '123')
                //$('#checkbox').append('ID: '   + $(this).find('id').text() + '<br/>')
                //               .append('Name ' + $(this).find('name').text() + '<br/>');
            });
        }, 'xml');                                     // указываем явно тип данных
    };


function add_poi(id){
                  // вешаем на клик по элементу с id = example-3
	$.get('constructor.html?poi_id=' + id + '&_ox&sid=' + sid, {}, function(xml){  // загрузку XML из файла example.xml

	$('#poi-print').html('');
        $(xml).find('route').find('pois').find('poi').each(function(){       // заполняем DOM элемент данными из XML
		$('#poi-print').append('ID = ' + $(this).find('id').text() + '  Name: ' + $(this).find('name').text()  + '<br/><br/>');
		//alert($(this).find('name').text());
		//alert('find');
        });


		sid = $(xml).find('route').find('sid').text();
	}, 'xml');                                     // указываем явно тип данных
	//alert('sid=' + sid + '  id=' +id);

};


function set_sid(v_sid)
{
	sid = sid;
}

function get_sid()
{
	return sid;
}
