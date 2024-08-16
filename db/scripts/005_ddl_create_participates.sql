--liquibase formatted sql
--changeset plahotinandrei:5
create table participates(
    id serial primary key,
    post_id int not null REFERENCES auto_post(id),
    user_id int not null REFERENCES auto_user(id),
    UNIQUE (post_id, user_id)
);