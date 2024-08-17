--liquibase formatted sql
--changeset plahotinandrei:7
create table car(
    id serial primary key,
    "name" varchar not null,
    engine_id int not null REFERENCES engine(id)
);