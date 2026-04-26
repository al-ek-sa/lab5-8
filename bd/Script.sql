create table coordinates (
    id serial primary key,
    x bigint not null,
    y float not null,
    unique(x, y),
    check(x <= 10),
    check (y > -644)
);

create type status as enum (
    'FIRED',
    'HIRED',
    'RECOMMENDED_FOR_PROMOTION',
    'PROBATION'
);

create type organization_type as enum (
    'PUBLIC',
    'COMMERCIAL',
    'GOVERNMENT',
    'OPEN_JOINT_STOCK_COMPANY',
    'TRUST'
);

create table address (
    id serial primary key,
    street varchar(250) not null,
    unique(street)
);


create table organization (
    id serial primary key,
    annual_turnover integer not null,
    type organization_type not null,
    address_id integer references address(id) not null,
    unique(annual_turnover, type, address_id),
    check(annual_turnover > 0)
);

create table worker (
    id serial primary key,
    worker_id varchar(25) not null,
    name varchar(100) not null,
    coordinates_id integer references coordinates(id),
    creation_date date not null default now(),
    salary float,
    start_date date not null,
    end_date date,
    status status not null,
    organization_id integer references organization(id),
    unique(worker_id),
    check(name != ''),
    check(salary > 0),
    check(end_date is null or start_date < end_date)
);




