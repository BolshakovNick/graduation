--liquibase formatted sql

--changeset Bolshakov:5
CREATE SEQUENCE dish_id_seq START WITH 100000;

CREATE TABLE dish
(
    id               BIGINT DEFAULT nextval('dish_id_seq') PRIMARY KEY,
    menu_id          BIGINT        NOT NULL,
    dish_name        VARCHAR(50)   NOT NULL,
    price            INTEGER       NOT NULL,
    CONSTRAINT dish_menu_id FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);
