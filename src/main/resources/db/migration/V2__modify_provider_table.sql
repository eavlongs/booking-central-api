ALTER TABLE "providers" ADD "issuer_url" VARCHAR(255) NOT NULL UNIQUE;
ALTER TABLE "providers" ADD "registration_id" VARCHAR(255) NOT NULL UNIQUE;