drop table `test`.`cloudmunch1`;
create table `test`.`cloudmunch1`(
	`username` varchar(20) not null,
	`password` varchar(20) not null,
	primary key (`username`)
);

insert into `test`.`cloudmunch1` (username,password) values("shraddha","cloudmunch");
insert into `test`.`cloudmunch1` (username,password) values("prathyusha","cloudmunch1");
insert into `test`.`cloudmunch1` (username,password) values("leela","cloudmunch2");

drop table `test`.`cloudmunch2`;

create table `test`.`cloudmunch2`(
	`customer` varchar(20) not null,
	primary key (`customer`)
);

insert into `test`.`cloudmunch2` (customer) values ("jamcracker");
insert into `test`.`cloudmunch2` (customer) values ("beyondsquare");
insert into `test`.`cloudmunch2` (customer) values ("svapas");
