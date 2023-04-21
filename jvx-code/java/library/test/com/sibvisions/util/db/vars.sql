create table orders
(
  id integer identity,
  name varchar(255) not null
)

create table streets
(
 id integer identity,
 name varchar(200) not null,
 constraint stre_uk unique(name)
)

insert into orders (id, name) values (0, 'zero');
insert into orders (id, name)
values (1, 'first');

insert into orders (id, name)
values (2, 'second');


${MAX} = select max(id) from orders;

insert into streets(id, name) values(5, ${MAX});

${RESULT} = select id from streets where name = ${MAX};