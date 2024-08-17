--liquibase formatted sql
--changeset plahotinandrei:6
create table engine(
    id serial primary key,
    "name" varchar not null
);