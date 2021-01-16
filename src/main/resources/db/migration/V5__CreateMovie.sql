CREATE TABLE Movie(id bigint constraint id primary key,name character varying(255) not null,
released int not null,director character varying(255) not null,rating int,available int,total int);
CREATE SEQUENCE movie_seq START 1;