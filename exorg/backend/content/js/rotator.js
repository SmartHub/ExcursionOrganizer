<!-- Автор Dylan Wagstaff, http://www.alohatechsupport.net -->
var intervalID;

function theRotator() {
	// Устанавливаем прозрачность всех картинок в 0
	$('div#rotator ul li').css({opacity: 0.0});
 
	// Берем первую картинку и показываем ее (по пути включаем полную видимость)
	$('div#rotator ul li:first').css({opacity: 1.0});
 
	// Вызываем функцию rotate для запуска слайдшоу, 5000 = смена картинок происходит раз в 5 секунд
	intervalID = setInterval('rotate()',5000);
}
 
function rotate(fast) {	
	// Берем первую картинку
	var current = ($('div#rotator ul li.show')?  $('div#rotator ul li.show') : $('div#rotator ul li:first'));
 
	// Берем следующую картинку, когда дойдем до последней начинаем с начала
	var next = ((current.next().length) ? ((current.next().hasClass('show')) ? $('div#rotator ul li:first') :current.next()) : $('div#rotator ul li:first'));	
 
	// Расскомментируйте, чтобы показвать картинки в случайном порядке
	// var sibs = current.siblings();
	// var rndNum = Math.floor(Math.random() * sibs.length );
	// var next = $( sibs[ rndNum ] );
 
	var delay = 1000;
	if (fast == true){
		delay = 0;
	}
	// Подключаем эффект растворения/затухания для показа картинок, css-класс show имеет больший z-index
	next.css({opacity: 0.0})
	.addClass('show')
	.animate({opacity: 1.0}, delay);
 
	// Прячем текущую картинку
	current.animate({opacity: 0.0}, delay)
	.removeClass('show');

    $("#selector").attr("value", $('div#rotator ul li.show').attr("id"));
};

function rotate_back(fast) {	
	// Берем первую картинку
	var current = ($('div#rotator ul li.show')?  $('div#rotator ul li.show') : $('div#rotator ul li:first'));
 
	// Берем следующую картинку, когда дойдем до последней начинаем с начала
	var next = ((current.prev().length) ? ((current.prev().hasClass('show')) ? $('div#rotator ul li:last') :current.prev()) : $('div#rotator ul li:last'));	
 
	// Расскомментируйте, чтобы показвать картинки в случайном порядке
	// var sibs = current.siblings();
	// var rndNum = Math.floor(Math.random() * sibs.length );
	// var next = $( sibs[ rndNum ] );
 
	var delay = 1000;
	if (fast == true){
		delay = 0;
	}
	// Подключаем эффект растворения/затухания для показа картинок, css-класс show имеет больший z-index
	next.css({opacity: 0.0})
	.addClass('show')
	.animate({opacity: 1.0}, delay);
 
	// Прячем текущую картинку
	current.animate({opacity: 0.0}, delay)
	.removeClass('show');

	$("#selector").attr("value", $('div#rotator ul li.show').attr("id"));
};

function init_rotator() {
	$('div#rotator ul li:first').addClass('show');
	$("#selector").attr("value", $('div#rotator ul li.show').attr("id"));
};

 
$(document).ready(function() {		
	// Запускаем слайдшоу
	theRotator();

    $("#next").click(function() {
        //alert("Hello world!");
	clearInterval(intervalID);
	rotate(true);
    });

    $("#prev").click(function() {
        //alert("Hello world!");
	clearInterval(intervalID);
	rotate_back(true);
    });

});

function selectorChanged()
{
    //alert("selectorChanged();");

    clearInterval(intervalID);

    $('div#rotator ul li.show')
    .animate({opacity: 0.0}, 0)
    .removeClass('show');

    var id = $("#selector").attr("value");
    $('div#rotator ul li#' + id)
    .addClass('show')
	.animate({opacity: 1.0}, 0);

}

