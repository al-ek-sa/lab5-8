create table "user" (
                        login varchar(25) primary key ,
                        password varchar(40),
                        email varchar(30),
                        check (char_length(login) > 8 and char_length(password) > 8)
);

create table worker (
                        worker_id varchar(25) primary key,
                        name varchar(100),
                        user_login integer references "user"(login),
                        check (worker_id is not null),
                        check (name is not null),
                        unique(worker_id)
);

create index idx_worker_worker_id on worker(worker_id);
create index idx_worker_name on worker(name);
create index idx_user_login on "user"(login);
create index idx_user_password on "user"(password);
create index idx_user_login_password on "user"(login, password);