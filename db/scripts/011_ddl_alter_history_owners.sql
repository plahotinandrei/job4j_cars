--liquibase formatted sql
--changeset plahotinandrei:11
alter table history_owners add column startAt timestamp not null;
alter table history_owners add column endAt timestamp not null;