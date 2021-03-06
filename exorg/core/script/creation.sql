/*** СОЗДАНИЕ БАЗЫ excursion_organizer ***/

DROP DATABASE IF EXISTS excursion_organizer;
CREATE DATABASE excursion_organizer CHARACTER SET = utf8;
USE excursion_organizer;

/***** СОЗДАНИЕ ТАБЛИЦ *****/

/*** ГЛАВНЫЙ ДОМЕН ***/

/* ОБЩИЕ */

SET CHARSET utf8;

/* таблица городов */
DROP TABLE IF EXISTS city;
CREATE TABLE city (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(100) NOT NULL,
    ne_lat DECIMAL(11, 8),
    ne_lng DECIMAL(11, 8),
    sw_lat DECIMAL(11, 8),
    sw_lng DECIMAL(11, 8),

    lat_subdiv INT,
    lng_subdiv INT
) DEFAULT CHARACTER SET=utf8;


/* ДОСТОПРИМЕЧАТЕЛЬНОСТИ. Замечание: poi - place of interest */

/* таблица с типами достопримечательностей (музей, выставочный зал и т.п.) */
DROP TABLE IF EXISTS poi_type;
CREATE TABLE poi_type (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    guess_rx    VARCHAR(300) NULL,
	icon        VARCHAR(100) NOT NULL
) DEFAULT CHARACTER SET=utf8;

/* таблица достопримечательностей */
DROP TABLE IF EXISTS place_of_interest;
CREATE TABLE place_of_interest (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    TEXT NOT NULL,
    type_id INT NULL,
    city_id INT NULL,

    address VARCHAR(300) NULL,
    lat     DECIMAL(11, 8),
    lng     DECIMAL(11, 8),

    url 	VARCHAR(300) NULL,

    cluster_id INT,
    is_head INT,

    sq_n    INT
	
    /*
    photo	BLOB NULL,
	FOREIGN KEY (type_id) REFERENCES poi_type(id) ON UPDATE CASCADE,
	FOREIGN KEY (city_id) REFERENCES city(id)     ON UPDATE CASCADE
    */
) DEFAULT CHARACTER SET=utf8;

DROP TABLE IF EXISTS poi_descr;
CREATE TABLE poi_descr (
    poi_id  INT,
    descr   TEXT,
    src_url VARCHAR(300),

    FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;

DROP TABLE IF EXISTS poi_image;
CREATE TABLE poi_image (
    poi_id  INT,
    img_url VARCHAR(300),

    FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) DEFAULT CHARACTER SET=utf8;


/******************************************************************************/
/* Do not look here until its time comes :) */
/* пункты питания */
DROP TABLE IF EXISTS cafe;
CREATE TABLE cafe (
	id         INT AUTO_INCREMENT PRIMARY KEY,
	name       VARCHAR(100) NOT NULL,
	type_id    INT NULL, /* тип места для питания (ресторан, кафе и т.п.) */
    /*cuisine_id INT NULL,  тип кухни */
    cuisine    VARCHAR(100), /* the temporary solution*/
    url 	   VARCHAR(100) NULL,
    city_id    INT NULL,
	
	descr	   TEXT NULL,
    descr_src  VARCHAR(100) NULL

    /*
	FOREIGN KEY (type_id) 	 REFERENCES cafe_type(id) ON UPDATE CASCADE,
	FOREIGN KEY (cuisine_id) REFERENCES cuisine(id)	  ON UPDATE CASCADE,
	FOREIGN KEY (city_id) 	 REFERENCES city(id) 	  ON UPDATE CASCADE
    */
) DEFAULT CHARACTER SET=utf8;

DROP TABLE IF EXISTS cafe_address;
CREATE TABLE cafe_address (
    cafe_id    INT,

    city_id    INT,
    address    VARCHAR(300),
    lat        DECIMAL(11, 8),
    lng        DECIMAL(11, 8)
) DEFAULT CHARACTER SET=utf8;

DROP TABLE IF EXISTS poi_distance;
CREATE TABLE poi_distance (
    poi_id1     INT NOT NULL,
    poi_id2     INT NOT NULL,
    distance    FLOAT NOT NULL

    /*
	FOREIGN KEY (poi_id1) REFERENCES place_of_interest(id) ON UPDATE CASCADE,
	FOREIGN KEY (poi_id2) REFERENCES place_of_interest(id) ON UPDATE CASCADE
    */
) DEFAULT CHARACTER SET=utf8;

/* Таблицы для рекомендуемых маршрутов*/

/* таблица рекомендованных маршрутов с описаниями и протяженностью (duration в метрах)*/
DROP TABLE IF EXISTS route_recommended;
CREATE TABLE route_recommended (
    id           INT AUTO_INCREMENT PRIMARY KEY,
	name         VARCHAR(100) NOT NULL,
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

/* Заливаем информацию, которую мы не сможем получить автоматически */
\. preload.sql
/*\. distances.sql */
