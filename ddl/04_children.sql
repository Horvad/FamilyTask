CREATE TABLE IF NOT EXISTS app.children
(
    id bigint NOT NULL,
    version bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT children_pkey PRIMARY KEY (id)
)