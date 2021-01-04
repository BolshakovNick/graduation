--liquibase formatted sql

--changeset Bolshakov:8
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
