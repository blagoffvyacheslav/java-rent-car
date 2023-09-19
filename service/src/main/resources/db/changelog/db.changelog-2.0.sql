--liquibase formatted sql

--changeset blagoff:1
--User

ALTER TABLE users

    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by  VARCHAR(255);


--Order

ALTER TABLE orders

    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by  VARCHAR(255);


--Damages

ALTER TABLE damage

    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by  VARCHAR(255);


--OrderDetails

ALTER TABLE order_details

    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by  VARCHAR(255);


--UserDetails

ALTER TABLE user_details

    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by  VARCHAR(255);

-- rollback drop all;