USE excursion_organizer;

SET CHARSET UTF8;

/* проверяем, что в базе больше 100 достопримечательностей */
SELECT IF(COUNT(*) < 101, 'failed', 'passed') AS test_1 FROM place_of_interest;


/* проверяем, что в базе по каждому типу достопримечательности есть хотя бы один объект */
SELECT IF(used_poi_type_number < poi_type_number, 'failed', 'passed') AS test_2 FROM 

(SELECT COUNT(type_id) AS used_poi_type_number FROM 
	(SELECT type_id, COUNT(id) FROM place_of_interest 
		GROUP BY type_id HAVING type_id IS NOT NULL) 
	AS poi_number_by_type) 
AS used_poi_type_counting, 

(SELECT COUNT(id) AS poi_type_number FROM poi_type) 
AS poi_type_counting;
