CREATE TABLE IF NOT EXISTS users
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    username VARCHAR(64) NOT NULL UNIQUE ,
    first_name VARCHAR(64),
    last_name VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS payment
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    amount INT NOT NULL ,
    user_id uuid NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_chat
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    user_id uuid NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    chat_id uuid NOT NULL REFERENCES chat (id) ON DELETE CASCADE,
    UNIQUE (user_id, chat_id)
);

-- INSERT INTO users (username, first_name, last_name)
-- VALUES ('ivan@gmail.com', 'Ivan', 'Ivanov'),
--        ('petr@gmail.com', 'Petr', 'Petrov'),
--        ('sveta@gmail.com', 'Sveta', 'Svetikova');
--
-- INSERT INTO payment (amount, user_id)
-- VALUES (100, (SELECT id FROM users WHERE username = 'ivan@gmail.com')),
--        (300, (SELECT id FROM users WHERE username = 'ivan@gmail.com')),
--        (500, (SELECT id FROM users WHERE username = 'ivan@gmail.com')),
--        (250, (SELECT id FROM users WHERE username = 'petr@gmail.com')),
--        (600, (SELECT id FROM users WHERE username = 'petr@gmail.com')),
--        (500, (SELECT id FROM users WHERE username = 'petr@gmail.com')),
--        (400, (SELECT id FROM users WHERE username = 'sveta@gmail.com')),
--        (300, (SELECT id FROM users WHERE username = 'sveta@gmail.com'));
--
-- INSERT INTO chat (name)
-- VALUES ('java'),
--        ('database');
--
-- INSERT INTO user_chat(user_id, chat_id)
-- VALUES ((SELECT id FROM users WHERE username = 'petr@gmail.com'), (SELECT id FROM chat WHERE name = 'java')),
--        ((SELECT id FROM users WHERE username = 'sveta@gmail.com'), (SELECT id FROM chat WHERE name = 'java')),
--        ((SELECT id FROM users WHERE username = 'petr@gmail.com'), (SELECT id FROM chat WHERE name = 'database'));
