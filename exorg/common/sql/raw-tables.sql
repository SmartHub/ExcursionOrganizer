/* Таблицы, которые заполняет Miner */

/* Таблица с георграфической информацией о достопримечательностей */
/*
DROP TABLE IF EXISTS poi_raw_geo;
CREATE TABLE poi_raw_geo (
       poi_id  INT,
       address VARCHAR(300),
       lat     DECIMAL(11, 8),
       lng     DECIMAL(11, 8),

       FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;
*/

/* Таблица с описаниями достопримечательностей */


/* Таблица с фотографиями достопримечательностей */
