/********************************************************************************/

SET CHARSET utf8;

TRUNCATE city;
INSERT INTO city(id, name, ne_lat, ne_lng, sw_lat, sw_lng, lat_subdiv, lng_subdiv) VALUES
       (1, 'Санкт-Петербург', 60.2427816, 30.7600114, 59.6338076, 29.4247987, 20, 20);

TRUNCATE poi_type;
INSERT INTO poi_type(id, name, guess_rx, icon) VALUES 
       (1, 'прочее', NULL, 'img/icons/other.png'),
       (2, 'музей', '.*(музей).*', 'img/icons/museum.png'),
       (3, 'улица', '.*(улица|проспект|площадь).*', 'img/icons/street.png'),
       (4, 'памятник', '.*(памятник).*', 'img/icons/memorial.png'),
       (5, 'мост', '.*(мост).*', 'img/icons/bridge.png'),
       (6, 'здание', '.*(здание|храм|собор|дворец|дом).*', 'img/icons/building.png'),
       (7, 'парк', '.*(парк|сад).*', 'img/icons/park.png');

/********************************************************************************/

/* Load route info */

\. readyroute.data.sql

/********************************************************************************/
