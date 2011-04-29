USE excursion_organizer;

/* Удаление из базы достопримечательностей у которых не удалось определить координаты */
DELETE FROM place_of_interest WHERE lat IS NULL OR lng IS NULL OR lat = -1.0 OR lng = -1.0;
