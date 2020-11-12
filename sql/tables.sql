drop database if exists nwsupdater;

create database if not exists nwsupdater;

use nwsupdater;

drop table if exists alert;
drop table if exists received_alerts;
drop table if exists user;
drop table if exists location;
drop table if exists location_alerts;

create table if not exists alert
(
    id int auto_increment
        primary key,
    name varchar(100) null
);

create table if not exists received_alerts
(
    nws_id varchar(20) not null
        primary key,
    area_desc varchar(200) null,
    certainty varchar(20) null,
    urgency varchar(20) null,
    event varchar(20) null,
    headline varchar(100) null,
    description blob null
);

create table if not exists user
(
    id int auto_increment
        primary key,
    email varchar(30) not null,
    phone varchar(15) not null,
    password varbinary(20) not null
);

create table if not exists location
(
    id int auto_increment
        primary key,
    name varchar(40) not null,
    lat double not null,
    lon double not null,
    gridpoint_office varchar(4) not null,
    gridpoint_x int not null,
    gridpoint_y int not null,
    sms_enabled tinyint(1) not null,
    email_enabled tinyint(1) not null,
    owner_id int not null,
    constraint location_user_id_fk
        foreign key (owner_id) references user (id)
            on delete cascade
);

create table if not exists location_alerts
(
    location_id int not null,
    alert_id int not null,
    constraint location_alerts_pk
        unique (location_id),
    constraint location_alerts_pk_2
        unique (alert_id),
    constraint location_alerts_alert_id_fk
        foreign key (alert_id) references alert (id)
            on delete cascade,
    constraint location_alerts_location_id_fk
        foreign key (location_id) references location (id)
            on delete cascade
);

