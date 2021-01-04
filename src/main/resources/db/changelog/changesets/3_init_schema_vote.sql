--liquibase formatted sql

--changeset Bolshakov:3
CREATE SEQUENCE vote_id_seq START WITH 100000;

CREATE TABLE vote
(
    id               BIGINT DEFAULT nextval('vote_id_seq') PRIMARY KEY,
    voting_datetime  TIMESTAMP                   NOT NULL,
    user_id          BIGINT                      NOT NULL,
    restaurant_id    BIGINT                              ,
    voting_date DATE AS (CAST(voting_datetime AS DATE)),
    CONSTRAINT vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT vote_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX vote_unique_user_id_voting_datetime_idx ON vote (user_id, voting_date);
