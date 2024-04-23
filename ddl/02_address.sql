CREATE TABLE IF NOT EXISTS app.address
(
    id bigserial NOT NULL,
    version bigint NOT NULL,
    street text COLLATE pg_catalog."default" NOT NULL,
    number_house integer NOT NULL,
    number_flat integer NOT NULL,
    CONSTRAINT address_pkey PRIMARY KEY (id)
);

