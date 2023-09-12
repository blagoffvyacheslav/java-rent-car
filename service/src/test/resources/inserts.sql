INSERT INTO public.users (id, login, email, password, role, modified_by, created_at, created_by, modified_at)
VALUES (1, 'Admin', 'admin@gmail.com', 'qwerty', 'ADMIN', 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00'),
       (2, 'client', 'client@client.com', 'qwerty', 'CLIENT', 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));
INSERT INTO public.user_details (id, user_id, name, lastname, address, passport_number, phone, birthday, registration_date, modified_by, created_at, created_by, modified_at)
VALUES (1, '1', 'Kobelev', 'Semen', '21 Lebedyanskya st', '7212112390', '+1 720 123 45 90', '1995-07-22 00:00:00', '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00'),
       (2, '2', 'Vyacheslav', 'Blagov', '17 Lenin st', '7212112342', '+1 720 123 45 67', '1994-12-05 00:00:00', '2023-07-03 09:59:59', 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00');
SELECT SETVAL('user_details_id_seq', (SELECT MAX(id) FROM user_details));
INSERT INTO public.driver_license (user_details_id, number, issue_date, expired_date)
VALUES ('1', '12345AB', '2015-03-02 00:00:00', '2025-03-01 00:00:00'),
       ('2', '12345BD', '2014-03-02 00:00:00', '2024-12-01 00:00:00');
SELECT SETVAL('driver_license_id_seq', (SELECT MAX(id) FROM driver_license));
INSERT INTO public.model (id, name)
VALUES (1, 'Kia Rio'),
       (2, 'Volvo X70');
INSERT INTO public.car_rate (id, model_id, term, price)
VALUES (1, '1', 'HOURS', 1000),
       (2, '2','HOURS', 10000);
INSERT INTO public.car (id, model_id, serial_number, is_new)
VALUES (1, '1', '0123456', 'false'),
       (2, '2','ABC12345678', 'false');
INSERT INTO public.orders (id, date, user_id, car_id, insurance, order_status, amount, modified_by, created_at, created_by, modified_at)
VALUES (1, '2023-07-01 00:00:00', '1', '1', 'true', 'CONFIRMATION', 1020, 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00'),
       (2, '2023-07-02 00:00:00', '2', '2', 'true', 'PAYED', 10000, 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00');
INSERT INTO public.order_details (id, order_id, start_date, end_date, modified_by, created_at, created_by, modified_at)
VALUES (1, '1', '2023-07-02 00:00:00', '2023-07-03 00:00:00', 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00'),
        (2, '2', '2023-07-10 00:00:00', '2023-07-11 23:59:00', 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00');

INSERT INTO public.damage (id, order_id, description, amount, modified_by, created_at, created_by, modified_at)
VALUES (1, '1', 'side scratch', 100, 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00'),
       (2, '2', 'broken headlight', 50, 1, '2023-07-03 10:00:00', 1, '2023-07-03 10:00:00');