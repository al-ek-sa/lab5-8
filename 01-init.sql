CREATE TABLE "user" (
                        id SERIAL PRIMARY KEY,
                        login VARCHAR(25),
                        password VARCHAR(40),
                        email VARCHAR(30),
                        CHECK (CHAR_LENGTH(login) > 8 AND CHAR_LENGTH(password) > 8),
                        UNIQUE (login)
);

CREATE TABLE worker (
                        worker_id VARCHAR(25) PRIMARY KEY,
                        name VARCHAR(100),
                        user_id INTEGER REFERENCES "user"(id),
                        CHECK (worker_id IS NOT NULL),
                        CHECK (name IS NOT NULL),
                        UNIQUE(worker_id)
);

CREATE INDEX idx_worker_worker_id ON worker(worker_id);
CREATE INDEX idx_worker_name ON worker(name);
CREATE INDEX idx_user_login ON "user"(login);
CREATE INDEX idx_user_password ON "user"(password);
CREATE INDEX idx_user_login_password ON "user"(login, password);

ALTER TABLE "user" ADD COLUMN IF NOT EXISTS login_attempts INTEGER DEFAULT 0;
ALTER TABLE "user" ADD COLUMN IF NOT EXISTS locked_until TIMESTAMP DEFAULT NULL;

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    code VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    attempts INTEGER DEFAULT 0,
    used BOOLEAN DEFAULT FALSE
    );

CREATE INDEX idx_reset_tokens_user_id ON password_reset_tokens(user_id);
CREATE INDEX idx_reset_tokens_code ON password_reset_tokens(code);