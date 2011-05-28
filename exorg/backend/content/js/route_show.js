function init_points()
{
//    alert(document.location.href);
	PoiList = [];
    var href = document.location.href;
    if (href.substr(href.length - 4, 4) == 'html')
    {
        suf = '?_ox';
    }
    else
    {
        suf = '&_ox';
    }
	$.get(document.location.href + suf, {}, function(xml)
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
            //alert(Poi.Id + "; " + Poi.Name + " ; " + Poi.Lat + "; " + Poi.Lng);
			PoiList.push(Poi);
        	});                                                            
		calculate_route_unoptimal(PoiList);
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
            Lng : $(this).attr("lng"),
            Url : "poi.html?id=" + $(this).attr("poi-id")
        };
        //alert(Poi.Id + "; " + Poi.Name + " ; " + Poi.Lat + 3"; " + Poi.Lng);
        PoiList.push(Poi);
        //alert(Poi.Id + " " + Poi.Name + " " + Poi.Lat + " " + Poi.Lng + " " + Poi.Url);
    });

    //alert('update');
    PoiList.sort(Comparator);
    calculate_route_unoptimal(PoiList);
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