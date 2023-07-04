INSERT INTO public.users (login, email, password, role)
VALUES ('Admin', 'admin@gmail.com', 'qwerty', 'ADMIN'),
       ('client', 'client@client.com', 'qwerty', 'CLIENT');
INSERT INTO public.user_details (user_id, name, lastname, address, passport_number, phone, birthday, registration_date)
VALUES ('1', 'Kobelev', 'Semen', '21 Lebedyanskya st', '7212112390', '+1 720 123 45 90', '1995-07-22 00:00:00', '2023-07-03 10:00:00'),
       ('2', 'Vyacheslav', 'Blagov', '17 Lenin st', '7212112342', '+1 720 123 45 67', '1994-12-05 00:00:00', '2023-07-03 09:59:59');
INSERT INTO public.driver_license (user_details_id, number, issue_date, expired_date)
VALUES ('1', '12345AB', '2015-03-02 00:00:00', '2025-03-01 00:00:00'),
       ('2', '12345BD', '2014-03-02 00:00:00', '2024-12-01 00:00:00');
INSERT INTO public.model (name)
VALUES ('Kia Rio'),
       ('Volvo X70');
INSERT INTO public.car_rate (model_id, term, price)
VALUES ('1', 'HOURS', 1000),
       ('2','HOURS', 10000);
INSERT INTO public.car (model_id, serial_number, is_new)
VALUES ('1', '0123456', 'false'),
       ('2','ABC12345678', 'false');
INSERT INTO public.orders (date, user_id, car_id, insurance, order_status, amount)
VALUES ('2023-07-01 00:00:00', '1', '1', 'true', 'CONFIRMATION', 1020),
       ('2023-07-02 00:00:00', '2', '2', 'true', 'PAYED', 10000);
INSERT INTO public.order_details (order_id, start_date, end_date)
VALUES ('1', '2023-07-02 00:00:00', '2023-07-03 00:00:00'),
        ('2', '2023-07-10 00:00:00', '2023-07-11 23:59:00');

INSERT INTO public.damage (order_id, description, amount)
VALUES ('1', 'side scratch', 100),
       ('2', 'broken headlight', 50);