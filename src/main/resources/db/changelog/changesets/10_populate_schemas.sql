--liquibase formatted sql

--changeset Bolshakov:10

INSERT INTO users (id, user_name, email, password, role, enabled)
VALUES (1, 'Username', 'test@email.com', '2WTcIqpSpWNPzxYzKYUBmVWLNXDjHowpgQjn4X0W/RA=', 'ADMIN', true);

INSERT INTO restaurant(id, restaurant_name, description)
VALUES (1, 'restaurant1', 'description1');

INSERT INTO restaurant(id, restaurant_name, description)
VALUES (2, 'restaurant2', 'description2');

INSERT INTO restaurant(id, restaurant_name, description)
VALUES (3, 'restaurant3', 'description3');
