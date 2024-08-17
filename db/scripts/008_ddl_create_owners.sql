--liquibase formatted sql
--changeset plahotinandrei:8
create table owners(
    id serial primary key,
    "name" varchar not null,
    user_id int not null REFERENCES auto_user(id)
);