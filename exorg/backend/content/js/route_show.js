function init_points()
{
	var PoiList = [];

	$.get(document.location.href + '&_ox', {}, function(xml)
	{
        $(xml).find('route_point').each(function()
		{
			Poi =
			{
				Id : $(this).attr("poi-id"),
				Name : $(this).find('name').text(),
				Lat : $(this).attr("lat"),
				Lng : $(this).attr("lng")
			};
            //alert(Poi.Id + "; " + Poi.Name + " ; " + Poi.Lat + "; " + Poi.Lng);
			PoiList.push(Poi);
        	});
		
		calculate_route(PoiList);
		/* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */

	}, 'xml'); // указываем явно тип данных
};

function update_points()
{
	var PoiList = [];

    $('input.number').each(function()
    {
        Poi =
        {
            Order : $(this).attr("value"),
            Id : $(this).attr("poi-id"),
            Name : $(this).attr('name'),
            Lat : $(this).attr("lat"),
            Lng : $(this).attr("lng")
        };
        //alert(Poi.Id + "; " + Poi.Name + " ; " + Poi.Lat + 3"; " + Poi.Lng);
        PoiList.push(Poi);
        //alert(Poi.Id + " " + Poi.Name + " " + Poi.Lat + " " + Poi.Lng);
    });

    PoiList.sort(Comparator);
    calculate_route(PoiList);
    /* в этом месте будет вызвана функция для отрисовки маршрута с тем же массивом объектов в кач-ве параметра */

};

function Comparator(i, ii) {
    if (i.Order > ii.Order)
        return -1;
    else if (i.Order < ii.Order)
        return 1;
    else
        return 0;
}