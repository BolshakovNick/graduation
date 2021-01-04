DROP TABLE IF EXISTS dish;
DROP SEQUENCE IF EXISTS dish_id_seq;

DROP INDEX IF EXISTS menu_unique_restaurant_id_menu_datetime_idx;
DROP TABLE IF EXISTS menu;
DROP SEQUENCE IF EXISTS menu_id_seq;

DROP INDEX IF EXISTS vote_unique_user_id_voting_datetime_idx;
DROP TABLE IF EXISTS vote;
DROP SEQUENCE IF EXISTS vote_id_seq;

DROP INDEX IF EXISTS restaurant_unique_restaurant_name_idx;
DROP TABLE IF EXISTS restaurant;
DROP SEQUENCE IF EXISTS restaurant_id_seq;

DROP INDEX IF EXISTS verification_token_unique_user_id_idx;
DROP INDEX IF EXISTS verification_token_unique_uuid_idx;
DROP TABLE IF EXISTS verification_token;
DROP SEQUENCE IF EXISTS verification_token_id_seq;

DROP INDEX IF EXISTS users_unique_email_idx;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_id_seq;

CREATE SEQUENCE user_id_seq START WITH 100000;
CREATE TABLE users
(
    id               LONG DEFAULT user_id_seq.nextval PRIMARY KEY,
    user_name        VARCHAR(50)                   NOT NULL,
    email            VARCHAR(256)                  NOT NULL,
    password         VARCHAR                       NOT NULL,
    role             VARCHAR(5)                    CHECK (role = 'USER' OR role = 'ADMIN'),
    enabled          BOOLEAN                       DEFAULT FALSE
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE SEQUENCE restaurant_id_seq START WITH 100000;

CREATE TABLE restaurant
(
    id               BIGINT DEFAULT restaurant_id_seq.nextval PRIMARY KEY,
    restaurant_name        VARCHAR(50)        NOT NULL,
    description            VARCHAR
);
CREATE UNIQUE INDEX restaurant_unique_restaurant_name_idx ON restaurant (restaurant_name);

CREATE SEQUENCE vote_id_seq START WITH 100000;

CREATE TABLE vote
(
    id               BIGINT DEFAULT nextval('vote_id_seq') PRIMARY KEY ,
    voting_datetime  TIMESTAMP                   NOT NULL,
    user_id          BIGINT                      NOT NULL,
    restaurant_id    BIGINT                      NOT NULL,
    voting_date DATE AS (CAST(voting_datetime AS DATE)),
    CONSTRAINT vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT vote_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX vote_unique_user_id_voting_datetime_idx ON vote (user_id, voting_date);

CREATE SEQUENCE menu_id_seq START WITH 100000;

CREATE TABLE menu
(
    id               BIGINT DEFAULT nextval('menu_id_seq') PRIMARY KEY ,
    menu_date        DATE                    NOT NULL,
    restaurant_id    BIGINT                  NOT NULL,
    CONSTRAINT menu_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX menu_unique_restaurant_id_menu_datetime_idx ON menu (restaurant_id, menu_date);

CREATE SEQUENCE dish_id_seq START WITH 100000;

CREATE TABLE dish
(
    id               BIGINT DEFAULT nextval('dish_id_seq') PRIMARY KEY ,
    menu_id          BIGINT        NOT NULL,
    dish_name        VARCHAR(50)   NOT NULL,
    price            INTEGER       NOT NULL,
    CONSTRAINT dish_menu_id_fkey FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);

CREATE SEQUENCE verification_token_id_seq START WITH 100000;

CREATE TABLE verification_token
(
    id               BIGINT DEFAULT nextval('verification_token_id_seq') PRIMARY KEY,
    uuid             VARCHAR        NOT NULL,
    user_id          BIGINT         NOT NULL,
    expiry_date      TIMESTAMP      NOT NULL,
    CONSTRAINT verification_token_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX verification_token_unique_user_id_idx ON verification_token (user_id);
CREATE UNIQUE INDEX verification_token_unique_uuid_idx ON verification_token (uuid);