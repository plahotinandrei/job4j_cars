--liquibase formatted sql
--changeset plahotinandrei:14
create table models(
    id serial primary key,
    "name" varchar not null unique
);