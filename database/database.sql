drop database if exists trylma;
create database trylma;
use trylma;

create table moves (
	id int primary key auto_increment,
	port int,
	game int,
	board varchar(510)
);