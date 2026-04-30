create table worker (
                        worker_id varchar(25) primary key,
                        name varchar(100),
                        user_id integer references "user"(id),
                        check (worker_id is not null),
                        check (name is not null),
                        unique(worker_id)
);

create table "user" (
                      id serial primary key,
                      login varchar(25),
                      password varchar(40),
                      name varchar(25),
                      email varchar(30),
                      check (char_length(login) > 8 and char_length(password) > 8),
                      unique (name),
                      unique (login)
);


create index idx_worker_worker_id on worker(worker_id);
create index idx_worker_name on worker(name);
create index idx_user_login on "user"(login);
create index idx_user_password on "user"(password);
create index idx_user_login_password on "user"(login, password);