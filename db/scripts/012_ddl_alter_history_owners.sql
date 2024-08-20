--liquibase formatted sql
--changeset plahotinandrei:12
alter table history_owners drop column endAt;
alter table history_owners add column endAt timestamp;