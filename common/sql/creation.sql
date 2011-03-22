/*** СОЗДАНИЕ БАЗЫ базы excursion_organizer ***/

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
       sw_lng DECIMAL(11, 8)
) DEFAULT CHARACTER SET=utf8;


/* ДОСТОПРИМЕЧАТЕЛЬНОСТИ. Замечание: poi - place of interest */

/* таблица с типами достопримечательностей (музей, выставочный зал и т.п.) */
DROP TABLE IF EXISTS poi_type;
CREATE TABLE poi_type (
       id 	INT AUTO_INCREMENT PRIMARY KEY,
       name 	VARCHAR(100) NOT NULL,
       guess_rx VARCHAR(300) NULL
) DEFAULT CHARACTER SET=utf8;

/*
DROP TABLE IF EXISTS poi_type_heuristics;
CREATE TABLE poi_type_heuristics (
       type_id INT,
       keyword VARCHAR(100),

       FOREIGN KEY (type_id) REFERENCES poi_type(id) ON UPDATE RESTRICT ON DELETE RESTRICT
);
*/

/* таблица достопримечательностей */
DROP TABLE IF EXISTS place_of_interest;
CREATE TABLE place_of_interest (
	id 	INT AUTO_INCREMENT PRIMARY KEY,
	name 	TEXT NOT NULL,
	type_id INT NULL,
	city_id INT NULL,

	address VARCHAR(300) NULL,
	url 	VARCHAR(300) NULL,
	
	photo	BLOB NULL,
	FOREIGN KEY (type_id) REFERENCES poi_type(id) ON UPDATE CASCADE,
	FOREIGN KEY (city_id) REFERENCES city(id)     ON UPDATE CASCADE
) DEFAULT CHARACTER SET=utf8;

\. common/script/raw-tables.sql