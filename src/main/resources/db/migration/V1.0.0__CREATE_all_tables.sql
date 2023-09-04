

CREATE TABLE IF NOT EXISTS public.badlanguage
(
    id bigint NOT NULL,
    word character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT badlanguage_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.badlanguage
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.maliciouswebsite
(
    id bigint NOT NULL,
    url character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT maliciouswebsite_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.maliciouswebsite
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public.maliciousprocess
(
    id bigint NOT NULL,
    nameexe character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT maliciousprocess_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.maliciousprocess
    OWNER to postgres;


-- Table: public.maliciousport

-- DROP TABLE IF EXISTS public.maliciousport;

CREATE TABLE IF NOT EXISTS public.maliciousport
(
    id bigint NOT NULL,
    vulnarablebanners character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT maliciousport_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.maliciousport
    OWNER to postgres;




CREATE TABLE IF NOT EXISTS public.image
(
    id bigint NOT NULL,
    productimg character varying(255) COLLATE pg_catalog."default",
    base64img oid,
    CONSTRAINT image_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.image
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.alert
(
    data_cadastro date NOT NULL,
    id bigint NOT NULL,
    image_id bigint,
    pcid character varying(255) COLLATE pg_catalog."default",
    processos character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT alert_pkey PRIMARY KEY (id),
    CONSTRAINT fko1li43seqcas6ro7r5hpl97ro FOREIGN KEY (image_id)
        REFERENCES public.image (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.alert
    OWNER to postgres;