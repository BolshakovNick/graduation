--liquibase formatted sql

--changeset Bolshakov:4
CREATE SEQUENCE menu_id_seq START WITH 100000;

CREATE TABLE menu
(
    id               BIGINT DEFAULT nextval('menu_id_seq') PRIMARY KEY,
    menu_date        DATE                    NOT NULL,
    restaurant_id    BIGINT                  NOT NULL,
    CONSTRAINT menu_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX menu_unique_restaurant_id_menu_datetime_idx ON menu (restaurant_id, menu_date);
