create index idx_worker_worker_id on worker(worker_id);
create index idx_worker_name on worker(name);
create index idx_user_login on user(login);
create index idx_user_password on user(password);
create index idx_user_login_password on user(login, password);