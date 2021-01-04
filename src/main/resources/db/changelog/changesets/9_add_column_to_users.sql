--liquibase formatted sql

--changeset Bolshakov:9

ALTER TABLE users ADD COLUMN enabled BOOLEAN DEFAULT true;