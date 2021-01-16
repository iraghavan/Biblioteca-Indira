CREATE TABLE Book(id bigint constraint book_pk primary key, name character varying(255) not null, author character varying(255), published int, available boolean);
CREATE SEQUENCE book_seq START 1;