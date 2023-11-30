CREATE SCHEMA IF NOT EXISTS "source";

DROP TABLE IF EXISTS source."D_IMAGE_SOURCE" CASCADE;

CREATE TABLE source."D_IMAGE_SOURCE"
(
	id bigserial NOT NULL,
	name varchar(200) NULL,
	"desc" varchar(500) NULL,
    "realm" varchar(500) NULL,
	"ip" VARCHAR(255),
	"port" INTEGER,
	"cameraUser" VARCHAR(255),
	"cameraPassword" VARCHAR(255),
	"type" VARCHAR(16), -- DAHUA_5, DAHUA_12, GOOGLE_DRIVE, LOCAL
	"longitude" FLOAT,
	"latitude" FLOAT,
    "recorded" VARCHAR(255), -- jabłko, lipa
	"directory" VARCHAR(255),
	"serialNumber" VARCHAR(255),
	"producer" VARCHAR(255),
	"zoom" VARCHAR(255),
	"place" VARCHAR(255),
	"installationAt" TIMESTAMP WITH TIME ZONE NULL,
	"azimuth" INTEGER,
	"inclination" INTEGER,
	"directoryRegex" VARCHAR(255),
	"stationId" BIGINT NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"userId" VARCHAR(255) NOT NULL,
	"createdAt" timestamp with time zone NOT NULL,
	"modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE source."D_IMAGE_SOURCE" ADD CONSTRAINT "PK_D_IMAGE_SOURCE"	PRIMARY KEY (id);