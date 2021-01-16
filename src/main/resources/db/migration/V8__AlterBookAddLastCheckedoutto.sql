ALTER TABLE Book add column lastcheckedoutto character varying(8), add constraint book_user_fk
foreign key(lastcheckedoutto) references useraccount(librarynumber);