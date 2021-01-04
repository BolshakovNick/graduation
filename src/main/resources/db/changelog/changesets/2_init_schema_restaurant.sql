--liquibase formatted sql

--changeset Bolshakov:2
CREATE SEQUENCE restaurant_id_seq START WITH 100000;

CREATE TABLE restaurant
(
    id               BIGINT DEFAULT nextval('restaurant_id_seq') PRIMARY KEY,
    restaurant_name        VARCHAR(50)        NOT NULL,
    description            VARCHAR
);
CREATE UNIQUE INDEX restaurant_unique_restaurant_name_idx ON restaurant (restaurant_name);
