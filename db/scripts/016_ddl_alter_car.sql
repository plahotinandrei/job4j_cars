--liquibase formatted sql
--changeset plahotinandrei:16
alter table car add column model_id int;

alter table car
add constraint car_model_id_fkey
foreign key (model_id)
references models (id);