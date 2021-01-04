--liquibase formatted sql

--changeset Bolshakov:7

ALTER TABLE dish ALTER column price TYPE BIGINT;