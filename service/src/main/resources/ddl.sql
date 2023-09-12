CREATE DATABASE cartime

CREATE TABLE IF NOT EXISTS model
(
    id   BIGSERIAL      PRIMARY KEY,
    name VARCHAR(255)   NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS car_rate
(
    id           BIGSERIAL      PRIMARY KEY,
    model_id     BIGINT,
    name         VARCHAR(255)   NOT NULL,
    term         VARCHAR(32) NOT NULL DEFAULT 'HOURS',
    price        NUMERIC(10, 2) NOT NULL,
    CONSTRAINT car_rate_model_fk
    FOREIGN KEY (model_id) REFERENCES model (id)
    ON UPDATE CASCADE ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS car
(
    id            BIGSERIAL       PRIMARY KEY,
    model_id      BIGINT,
    serial_number VARCHAR(255)    NOT NULL UNIQUE,
    is_new        BOOLEAN DEFAULT 'FALSE',
    CONSTRAINT car_model_fk
    FOREIGN KEY (model_id) REFERENCES model (id)
    ON UPDATE CASCADE ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL          PRIMARY KEY,
    username VARCHAR(255)       NOT NULL UNIQUE,
    email    VARCHAR(255)       NOT NULL UNIQUE,
    password VARCHAR(255)       NOT NULL,
    role     VARCHAR(32)        NOT NULL DEFAULT 'CLIENT'
    );

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGSERIAL                                 PRIMARY KEY,
    date         TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    user_id      BIGINT                                    NOT NULL,
    car_id       BIGINT                                    NOT NULL,
    insurance    BOOLEAN                                   NOT NULL DEFAULT 'TRUE',
    order_status VARCHAR(32)                               NOT NULL,
    amount       NUMERIC(10, 2)                         NOT NULL,
    CONSTRAINT order_user_fk
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT orders_car_fk
    FOREIGN KEY (car_id) REFERENCES car (id)
    ON UPDATE CASCADE ON DELETE SET NULL
    );


CREATE TABLE IF NOT EXISTS order_details
(
    id                BIGSERIAL                   PRIMARY KEY,
    order_id          BIGINT                      NOT NULL UNIQUE,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT carrentaltime_order_fk
    FOREIGN KEY (order_id) REFERENCES orders (id)
    ON UPDATE CASCADE ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS user_details
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT                      NOT NULL,
    name              VARCHAR(128)                NOT NULL,
    lastname          VARCHAR(128)                NOT NULL,
    address           VARCHAR(255)                NOT NULL,
    passport_number   VARCHAR(255)                NOT NULL,
    phone             VARCHAR(32)                 NOT NULL,
    birthday          TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
    registration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT userdetails_user_fk
    FOREIGN KEY (user_id) REFERENCES users (id)
                                ON UPDATE CASCADE ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS driver_license
(
    id              BIGSERIAL                   PRIMARY KEY,
    user_details_id BIGINT                      NOT NULL,
    number          VARCHAR(32)                 NOT NULL UNIQUE,
    issue_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expired_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT driverlicense_user_details_fk
    FOREIGN KEY (user_details_id) references user_details (id)
                              ON UPDATE CASCADE ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS damage
(
    id            BIGSERIAL                   PRIMARY KEY,
    order_id      BIGINT                      NOT NULL,
    description   TEXT,
    amount        NUMERIC(10, 2),
    CONSTRAINT accident_order_fk
    FOREIGN KEY (order_id) REFERENCES orders (id)
    ON UPDATE CASCADE ON DELETE SET NULL
    );