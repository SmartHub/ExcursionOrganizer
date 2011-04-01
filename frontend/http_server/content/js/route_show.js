function init_points()
{
	var PoiList = [];

	$.get(document.location.href + '&_ox', {}, function(xml)
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
			//alert(Poi.Name);
        	});
		
		calculate_route(PoiList);
		/* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */

	}, 'xml'); // указываем явно тип данных
};
