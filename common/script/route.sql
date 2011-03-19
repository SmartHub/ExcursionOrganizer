/* Таблицы для рекомендуемых маршрутов*/

/* таблица рекомендованных маршрутов с описаниями и протяженностью (duration в метрах)*/
DROP TABLE IF EXISTS route_recommended;
CREATE TABLE route_recommended (
       id           INT AUTO_INCREMENT PRIMARY KEY,
       descr        TEXT NULL,
       count_point  INT NOT NULL,
       duration     INT NULL 
) DEFAULT CHARACTER SET=utf8;

/* Таблица с георграфической информацией о достопримечательностей */
DROP TABLE IF EXISTS route_poi;
CREATE TABLE route_poi (
       poi_id      INT NOT NULL,
       route_id    INT NOT NULL,
       order_num   INT,
	
       PRIMARY KEY (poi_id, route_id),
       FOREIGN KEY (poi_id)   REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
       FOREIGN KEY (route_id) REFERENCES route_recommended(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;
