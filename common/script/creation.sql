/*** СОЗДАНИЕ БАЗЫ базы excursion_organizer ***/
DROP DATABASE IF EXISTS excursion_organizer;
CREATE DATABASE excursion_organizer CHARACTER SET = utf8;
USE excursion_organizer;

/***** СОЗДАНИЕ ТАБЛИЦ *****/

/*** ГЛАВНЫЙ ДОМЕН ***/


/* ОБЩИЕ */

/* таблица городов */
DROP TABLE IF EXISTS city;
CREATE TABLE city (
	id 	INT AUTO_INCREMENT PRIMARY KEY,
	name 	VARCHAR(100) NOT NULL	
) DEFAULT CHARSET=utf8;


/* ДОСТОПРИМЕЧАТЕЛЬНОСТИ. Замечание: poi - place of interest */

/* таблица с типами достопримечательностей (музей, выставочный зал и т.п.) */
DROP TABLE IF EXISTS poi_type;
CREATE TABLE poi_type (
	id 	INT AUTO_INCREMENT PRIMARY KEY,
	name 	VARCHAR(100) NOT NULL	
) DEFAULT CHARSET=utf8;

/* таблица достопримечательностей */
DROP TABLE IF EXISTS place_of_interest;
CREATE TABLE place_of_interest (
	id 	INT AUTO_INCREMENT PRIMARY KEY,
	name 	VARCHAR(100) NOT NULL,
	type_id INT NULL,
	city_id INT NULL,

	address VARCHAR(300) NULL,
	url 	VARCHAR(100) NULL,
	descr	TEXT NULL,
        descr_src_url TEXT VARCHAR(100) NULL,
	photo	BLOB NULL,
	FOREIGN KEY (type_id) REFERENCES poi_type(id) 	ON UPDATE CASCADE,
	FOREIGN KEY (city_id) REFERENCES city(id)	ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/* цены на посещение достопримечательностей */
DROP TABLE IF EXISTS poi_cost;
CREATE TABLE poi_cost (
	id 	 INT AUTO_INCREMENT PRIMARY KEY,
	poi_id	 INT NOT NULL,
	category VARCHAR(50) NULL,	/* категория посетителей (пенсионер, студент и т.п.) */
	workday	 BOOL NULL, 		/* true - будний день, false - выходной/праздничный день */
	cost	 INT NOT NULL,	
	FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/* расписание работы достопримечательности */
DROP TABLE IF EXISTS poi_timetable;
CREATE TABLE poi_timetable (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	poi_id		INT NOT NULL,
	weekday		VARCHAR(10) NULL,	
	start_time	TIME NOT NULL,	
	finish_time  	TIME NOT NULL,
	FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;


/* ПУНКТЫ ПИТАНИЯ */

/* таблица с типами мест для питания (ресторан, кафе и т.п.) */
DROP TABLE IF EXISTS cafe_type;
CREATE TABLE cafe_type (
	id 	INT AUTO_INCREMENT PRIMARY KEY,
	name 	VARCHAR(100) NOT NULL	
) DEFAULT CHARSET=utf8;

/* таблица с видами кухни (азиатская, фастфуд и т.п.) */
DROP TABLE IF EXISTS cuisine;
CREATE TABLE cuisine (
	id 	INT AUTO_INCREMENT PRIMARY KEY,
	name 	VARCHAR(100) NOT NULL	
) DEFAULT CHARSET=utf8;

/* пункты питания */
DROP TABLE IF EXISTS cafe;
CREATE TABLE cafe (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	name 		VARCHAR(100) NOT NULL,
	type_id 	INT NULL, /* тип места для питания (ресторан, кафе и т.п.) */
	cuisine_id	INT NULL, /* тип кухни */
	city_id 	INT NULL,
	
	address VARCHAR(300) NULL,
	url 	VARCHAR(100) NULL,
	descr	TEXT NULL,
	FOREIGN KEY (type_id) 	 REFERENCES cafe_type(id) ON UPDATE CASCADE,
	FOREIGN KEY (cuisine_id) REFERENCES cuisine(id)	  ON UPDATE CASCADE,
	FOREIGN KEY (city_id) 	 REFERENCES city(id) 	  ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/* расписание работы пунктов питания */
DROP TABLE IF EXISTS cafe_timetable;
CREATE TABLE cafe_timetable (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	cafe_id		INT NOT NULL,
	workday	 	BOOL NULL, 		/* true - будний день, false - выходной/праздничный день */
	start_time	TIME NOT NULL,	
	finish_time  	TIME NOT NULL,
	FOREIGN KEY (cafe_id) REFERENCES cafe(id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;



/*** ДОМЕН "География" (geo) ***/


/* ОБЩИЕ */

/* таблица хранящее расстояние по широте, характерное для каждого города */
DROP TABLE IF EXISTS geo_city_lat_coeff;
CREATE TABLE geo_city_lat_coeff (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	city_id 	INT NOT NULL,
	lat_coeff	FLOAT NOT NULL,
	FOREIGN KEY (city_id) REFERENCES city(id) ON UPDATE CASCADE	
) DEFAULT CHARSET=utf8;


/* ДОСТОПРИМЕЧАТЕЛЬНОСТИ */

/* таблица координат достопримечательностей */
DROP TABLE IF EXISTS geo_poi_coordinate;
CREATE TABLE geo_poi_coordinate (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	poi_id		INT NOT NULL,
	lat		FLOAT NOT NULL,
	lng		FLOAT NOT NULL,
	FOREIGN KEY (poi_id) REFERENCES place_of_interest(id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/* таблица расстояний от достопримечательности до других ближайших достопримечательностей */
DROP TABLE IF EXISTS geo_poi_poi_distance;
CREATE TABLE geo_poi_poi_distance (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	poi_id1		INT NOT NULL,
	poi_id2		INT NOT NULL,
	distance	FLOAT NOT NULL,
	FOREIGN KEY (poi_id1) REFERENCES place_of_interest(id) ON UPDATE CASCADE,
	FOREIGN KEY (poi_id2) REFERENCES place_of_interest(id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/* таблица расстояний от достопримечательности до ближайших пунктов питания */
DROP TABLE IF EXISTS geo_poi_cafe_distance;
CREATE TABLE geo_poi_cafe_distance (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	poi_id		INT NOT NULL,
	cafe_id		INT NOT NULL,
	distance	FLOAT NOT NULL,
	FOREIGN KEY (poi_id)  REFERENCES place_of_interest(id) 	ON UPDATE CASCADE,
	FOREIGN KEY (cafe_id) REFERENCES cafe(id) 		ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;


/* ПУНКТЫ ПИТАНИЯ */

/* таблица координат пунктов питания */
DROP TABLE IF EXISTS geo_cafe_coordinate;
CREATE TABLE geo_cafe_coordinate (
	id 		INT AUTO_INCREMENT PRIMARY KEY,
	cafe_id		INT NOT NULL,
	lat		FLOAT NOT NULL,
	lng		FLOAT NOT NULL,
	FOREIGN KEY (cafe_id) REFERENCES cafe(id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;


