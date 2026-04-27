create table worker (
    id serial primary key,
    worker_id varchar(25),
    name varchar(100),
    user_id integer references user(id),
    check (worker_id != "" or worker_id is not null),
    check (name != "" or name is not null),
    unique(worker_id)
);

create table user (
    id serial primary key,
    login varchar(25),
    password varchar(25),
    check (char_length(login) > 8 and char_length(password) > 8),
    unique (login),
    unique (password)
);