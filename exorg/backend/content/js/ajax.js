function init_inner_frame()
{
	updateCheckBoxes();
	
}

function add_poi(id, checked)
{
	var action = '';
	if(checked == false)
	{
		action = '&action="delete"';
        $.get('constructor.html?poi_id=' + id + '&_ox' + action, {}, {}, 'xml');
        return;
	}
    action = '&action="add"';
	$.get('constructor.html?poi_id=' + id + '&_ox' + action, {}, {},'xml');
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
