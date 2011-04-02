/********************************************************************************/

SET CHARSET utf8;

TRUNCATE city;
INSERT INTO city(id, name, ne_lat, ne_lng, sw_lat, sw_lng) VALUES
       (1, 'Санкт-Петербург', 60.2427816, 30.7600114, 59.6338076, 29.4247987);

TRUNCATE poi_type;
INSERT INTO poi_type(id, name, guess_rx) VALUES 
       (1, 'прочее', NULL),
       (2, 'музей', '.*(музей).*'),
       (3, 'улица', '.*(улица|проспект|площадь).*'),
       (4, 'памятник', '.*(памятник).*'),
       (5, 'мост', '.*(мост).*'),
       (6, 'здание', '.*(здание|храм|собор|дворец|дом).*'),
       (7, 'парк', '.*(парк|сад).*');

/********************************************************************************/

/* Load route info */

\. route.sql
\. route.data.sql

/********************************************************************************/