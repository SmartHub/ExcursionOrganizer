/*Data for recommended routes*/

SET CHARSET UTF8;

TRUNCATE route_recommended;
INSERT INTO route_recommended (descr, count_point) VALUES
	('Там, где встречаются реки', 6),
	('Центральные площади', 6),
	('Невский проспект', 6),
	('Улица Зодчего России', 5),
	('Прогулка по Васильевскому Острову', 5);

/*Достопримечательности для каждого рекомендуемого маршрута*/
TRUNCATE route_poi;
INSERT INTO route_poi (poi_id, route_id, order_num) VALUES
	(130, 1, 1),
	(619, 1, 2),
	(23, 1, 3),
	(583, 1, 4),
	(587, 1, 5),
	(598, 1, 6),
	
	(665, 2, 1),
	(611, 2, 2),
	(284, 2, 3),
	(22, 2, 4),
	(604, 2, 5),
	(655, 2, 6),
	
	(271, 3, 1),
	(672, 3, 2),
	(427, 3, 3),
	(605, 3, 4),
	(614, 3, 5),
	(600, 3, 6),
	
	(23, 4, 1),
	(614, 4, 2),
	(644, 4, 3),
	(620, 4, 4),
	(682, 4, 5),
	
	(483, 5, 1),
	(603, 5, 2),
	(287, 5, 3),
	(700, 5, 4),
	(256, 5, 5);