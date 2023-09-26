DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    app       VARCHAR(255)                                        NOT NULL,
    uri       VARCHAR(255),
    ip        VARCHAR(15),
    timestamp TIMESTAMP
);
