--liquibase formatted sql
--changeset plahotinandrei:10
alter table auto_post add column car_id int;

alter table auto_post
add constraint car_car_id_fkey
foreign key (car_id)
references car (id);