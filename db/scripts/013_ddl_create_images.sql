--liquibase formatted sql
--changeset plahotinandrei:13
create table images(
    id serial primary key,
    "name" varchar not null unique,
    "path" varchar not null unique,
    auto_post_id  int references auto_post (id) not null
);