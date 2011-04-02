SET CHARSET utf8;

/* Пользовательские сессии */
DROP TABLE IF EXISTS user_session;
CREATE TABLE user_session (
       id      INT PRIMARY KEY

       /* The lonely field in this table. Is there some other
          information associated with a session? */
        
) DEFAULT CHARACTER SET=utf8;

DROP TABLE IF EXISTS user_route;
CREATE TABLE user_route (
       sid     INT,
       poi_id  INT,
       ord_num INT
) DEFAULT CHARACTER SET=utf8;
