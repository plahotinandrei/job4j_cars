--liquibase formatted sql
--changeset plahotinandrei:3
INSERT INTO auto_user (login, password) VALUES ('Ivanov', 'root');
INSERT INTO auto_user (login, password) VALUES ('Petrov', 'root');
INSERT INTO auto_user (login, password) VALUES ('Sidorov', 'root');