$(document).ready(
	function(){
		$("input.number").bind('change', check);
	}
);



function check(){
	var text = [];
	var text2 = [];
	$("#btn-ok").attr("disabled", false);
	$("input.number").each(
			function(){
				$(this).css( "background-color", "white" );
				text.push(this.value);
	});

	for(i = 0; i < text.length - 1; ++i){
		for(j = i + 1; j < text.length; ++j){
			if(text[i] == text[j]){
				text2.push(text[i]);
			}
		}
	}

	$("input.number").each(
			function(){
				for(i = 0; i < text2.length; ++i){
					if (this.value == text2[i]){
						$(this).css( "background-color", "red" );
						$("#btn-ok").attr("disabled", true);
					};
				}
	});

};
