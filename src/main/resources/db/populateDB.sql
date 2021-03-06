DELETE
FROM choice;
DELETE
FROM DISH;
DELETE
FROM users;
DELETE
FROM restaurant;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO restaurant (name)
VALUES ('Alchemist'),
       ('Direkte');

INSERT INTO users (name, email, password, role)
VALUES ('User', 'user@yandex.ru', 'password', 'USER'),
       ('Admin', 'admin@gmail.com', 'admin', 'ADMIN');

INSERT INTO dish (name, price, restaurant_id)
VALUES ('Omelette', 500, 100000),
       ('Cutlet', 1000, 100000),
       ('Balyk', 300, 100000),
       ('Paste', 100, 100000),
       ('Pizza', 430, 100000),
       ('Bun', 1000, 100001),
       ('Pudding', 510, 100001),
       ('Samosa', 300, 100001),
       ('Salad', 450, 100001);

INSERT INTO choice (user_id, restaurant_id)
VALUES (100002, 100000);

INSERT INTO choice (user_id, restaurant_id, date)
VALUES (100003, 100000, '2022-01-08'),
       (100002, 100001, '2022-01-08');
