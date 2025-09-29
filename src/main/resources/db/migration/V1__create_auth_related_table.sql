CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "providers"
(
    "id"         SERIAL PRIMARY KEY,
    "name"       VARCHAR(255) NOT NULL UNIQUE,
    "logo_url"   VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE "users"
(
    "id"         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "first_name" VARCHAR(100) NOT NULL,
    "last_name"  VARCHAR(100),
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE "user_providers"
(
    "id"                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "user_id"           UUID NOT NULL ,
    "provider_id"       INTEGER NOT NULL,
    "provider_given_id" VARCHAR(255) NOT NULL,
    "created_at"        TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at"        TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT "fk_user_providers_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_user_providers_provider_id" FOREIGN KEY ("provider_id") REFERENCES "providers" ("id"),

    CONSTRAINT "uq_user_providers_user_id_provider_id" UNIQUE ("user_id", "provider_id"),
    CONSTRAINT "uq_user_providers_provider_given_id_provider_id" UNIQUE ("provider_given_id", "provider_id")
);