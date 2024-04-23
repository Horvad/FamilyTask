CREATE TABLE IF NOT EXISTS app.parent
(
    id bigserial NOT NULL,
    version bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    id_address bigint NOT NULL,
    CONSTRAINT parent_pkey PRIMARY KEY (id),
        CONSTRAINT parent_id_address_fkey FOREIGN KEY (id_address)
            REFERENCES app.address (id) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION
)