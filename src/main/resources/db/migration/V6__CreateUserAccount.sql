    CREATE TABLE useraccount(librarynumber character varying(8) constraint user_pk primary key, password text, name character varying(255), email character varying(255), phone character varying(10));
create extension pgcrypto;