--liquibase formatted sql

--changeset Bolshakov:6

ALTER TABLE users ALTER column password TYPE VARCHAR;