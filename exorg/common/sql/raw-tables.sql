/* Таблицы, которые заполняет Miner */

/* Таблица с георграфической информацией о достопримечательностей */
DROP TABLE IF EXISTS poi_raw_geo;
CREATE TABLE poi_raw_geo (
       poi_id  INT,
       address VARCHAR(300),
       lat     DECIMAL(11, 8),
       lng     DECIMAL(11, 8),

       FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;

/* Таблица с описаниями достопримечательностей */
DROP TABLE IF EXISTS poi_raw_descr;
CREATE TABLE poi_raw_descr (
     poi_id  INT,
     descr   TEXT NULL,
     src_url VARCHAR(300) NULL,

     FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;

/* Таблица с фотографиями достопримечательностей */
DROP TABLE IF EXISTS poi_raw_images;
CREATE TABLE poi_raw_images (
     poi_id  INT,
     img_url VARCHAR(300),

     FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;