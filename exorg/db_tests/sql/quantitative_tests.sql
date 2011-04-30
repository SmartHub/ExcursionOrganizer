USE excursion_organizer;

SET CHARSET UTF8;

/* проверяем, что по id = 1 в таблице достопримечательностей лежат данные об Исаакиевском соборе */
SELECT IF(name <> 'Исаакиевский собор', 'failed', 'passed') AS test_1 FROM place_of_interest WHERE id = 1;

/* проверяем, что в базе ровно 7 типов достопримечательностей */
SELECT IF(COUNT(*) <> 7, 'failed', 'passed') AS test_2 FROM poi_type;


