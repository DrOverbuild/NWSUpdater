DROP DATABASE IF EXISTS nwsupdater;

CREATE DATABASE IF NOT EXISTS nwsupdater;

USE nwsupdater;

DROP TABLE IF EXISTS alert;
DROP TABLE IF EXISTS received_alerts;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS location_alerts;

CREATE TABLE IF NOT EXISTS alert
(
    id INT AUTO_INCREMENT
        PRIMARY KEY,
    name VARCHAR(100) NULL
);

CREATE TABLE IF NOT EXISTS received_alerts
(
    nws_id VARCHAR(20) NOT NULL
        PRIMARY KEY,
    area_desc VARCHAR(200) NULL,
    certainty VARCHAR(20) NULL,
    urgency VARCHAR(20) NULL,
    event VARCHAR(20) NULL,
    headline VARCHAR(100) NULL,
    description BLOB NULL
);

CREATE TABLE IF NOT EXISTS user
(
    id INT AUTO_INCREMENT
        PRIMARY KEY,
    email VARCHAR(30) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    password VARBINARY(20) NOT NULL
);

ALTER TABLE user ADD CONSTRAINT user_email_uindex
    UNIQUE (email);

CREATE TABLE IF NOT EXISTS location
(
    id INT AUTO_INCREMENT
        PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    lat DOUBLE NOT NULL,
    lon DOUBLE NOT NULL,
    gridpoint_office VARCHAR(4) NOT NULL,
    gridpoint_x INT NOT NULL,
    gridpoint_y INT NOT NULL,
    sms_enabled TINYINT(1) NOT NULL,
    email_enabled TINYINT(1) NOT NULL,
    owner_id INT NOT NULL,
    CONSTRAINT location_user_id_fk
        FOREIGN KEY (owner_id) REFERENCES user (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS location_alerts
(
    location_id INT NOT NULL,
    alert_id INT NOT NULL,
    CONSTRAINT location_alerts_pk
        unique (location_id),
    CONSTRAINT location_alerts_pk_2
        unique (alert_id),
    CONSTRAINT location_alerts_alert_id_fk
        FOREIGN KEY (alert_id) REFERENCES alert (id)
            on delete cascade,
    CONSTRAINT location_alerts_location_id_fk
        FOREIGN KEY (location_id) REFERENCES location (id)
            ON DELETE CASCADE
);

