DELETE
FROM choices;
DELETE
FROM meals;
DELETE
FROM users;
DELETE
FROM restaurants;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO restaurants (name)
VALUES ('Alchemist'),
       ('Direkte');

INSERT INTO users (name, email, password, role)
VALUES ('User', 'user@yandex.ru', 'password', 'USER'),
       ('Admin', 'admin@gmail.com', 'admin', 'ADMIN');

INSERT INTO meals (name, price, restaurant_id)
VALUES ('Omelette', 500, 100000),
       ('Cutlet', 1000, 100000),
       ('Balyk', 300, 100000),
       ('Paste', 100, 100000),
       ('Pizza', 430, 100000),
       ('Bun', 1000, 100001),
       ('Pudding', 510, 100001),
       ('Samosa', 300, 100001),
       ('Salad', 450, 100001);

INSERT INTO choices (user_id, restaurant_id)
VALUES (100002, 100000),
       (100003, 100000),
       (100002, 100001);
