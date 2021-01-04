--liquibase formatted sql

--changeset Bolshakov:1
CREATE SEQUENCE user_id_seq START WITH 100000;

CREATE TABLE users
(
    id               BIGINT DEFAULT nextval('user_id_seq') PRIMARY KEY,
    user_name        VARCHAR(50)        NOT NULL,
    email            VARCHAR(256)       NOT NULL,
    password         VARCHAR(50)        NOT NULL,
    role             VARCHAR(5)         CHECK (role = 'USER' OR role = 'ADMIN')
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);
