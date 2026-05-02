# Схема базы данных

## Таблица `"user"`

Хранит пользователей системы, их пароли (SHA‑1) и данные для блокировки.

| Поле | Тип | Описание                                                        |
|------|-----|-----------------------------------------------------------------|
| `id` | SERIAL | PRIMARY KEY                                                     |
| `login` | VARCHAR(25) | Логин пользователя, уникальный                                  |
| `password` | VARCHAR(40) | Хэш пароля (SHA‑1)                                              |
| `email` | VARCHAR(30) | Email пользователя                                              |
| `login_attempts` | INTEGER | Количество неудачных попыток входа (по умолчанию 0)             |
| `locked_until` | TIMESTAMP | Время до которого аккаунт заблокирован (NULL = не заблокирован) |

**Ограничения:**
- `CHECK (CHAR_LENGTH(login) > 8 AND CHAR_LENGTH(password) > 8)` — логин и пароль длиннее 8 символов
- `UNIQUE (login)` — логин уникален

---

## Таблица `worker`

Содержит объекты `Worker`, привязанные к пользователю.

| Поле | Тип | Описание |
|------|-----|----------|
| `worker_id` | VARCHAR(25) | PRIMARY KEY, уникальный идентификатор работника |
| `name` | VARCHAR(100) | Имя работника |
| `user_id` | INTEGER | Внешний ключ на `"user"(id)`, при удалении пользователя удаляются его объекты |

**Ограничения:**
- `NOT NULL` для `worker_id` и `name`
- `UNIQUE (worker_id)`

---
## Индексы

| Индекс | Таблица | Поля | Назначение |
|--------|---------|------|------------|
| `idx_worker_worker_id` | `worker` | `worker_id` | Быстрый поиск по ID работника |
| `idx_worker_name` | `worker` | `name` | Быстрый поиск по имени |
| `idx_user_login` | `"user"` | `login` | Ускорение поиска по логину |
| `idx_user_password` | `"user"` | `password` | Ускорение аутентификации |
| `idx_user_login_password` | `"user"` | `login, password` | Составной индекс для входа |
| `idx_reset_tokens_user_id` | `password_reset_tokens` | `user_id` | Поиск токенов по пользователю |
| `idx_reset_tokens_code` | `password_reset_tokens` | `code` | Быстрый поиск по коду |

---

## Логика восстановления пароля

1. Пользователь запрашивает восстановление → генерируется 6‑значный код.
2. Код сохраняется в `password_reset_tokens` с `expires_at = NOW() + INTERVAL '60 seconds'`.
3. Код отправляется на email.
4. Пользователь вводит код → проверяется:
    - Код существует
    - `used = FALSE`
    - `expires_at > NOW()`
5. При успешной проверке:
    - Пароль обновляется
    - Код помечается `used = TRUE`
    - `login_attempts = 0`, `locked_until = NULL`
6. При неверном вводе `attempts` увеличивается на 1. При `attempts >= 3` код аннулируется (удаляется или помечается `used = TRUE`).