create sequence User_SEQ start with 1 increment by 50;
create table User (id integer not null, login varchar(255) unique, password varchar(255), primary key (id));
