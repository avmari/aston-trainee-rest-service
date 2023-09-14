INSERT INTO users (username, first_name, last_name)
VALUES ('ivan@gmail.com', 'Ivan', 'Ivanov'),
       ('petr@gmail.com', 'Petr', 'Petrov'),
       ('sveta@gmail.com', 'Sveta', 'Svetikova'),
       ('vlad@gmail.com', 'Vlad', 'Vladikov'),
       ('kate@gmail.com', 'Kate', 'Smith');

INSERT INTO payment (amount, user_id)
VALUES (100, (SELECT id FROM users WHERE username = 'ivan@gmail.com')),
       (300, (SELECT id FROM users WHERE username = 'ivan@gmail.com')),
       (500, (SELECT id FROM users WHERE username = 'ivan@gmail.com')),
       (250, (SELECT id FROM users WHERE username = 'petr@gmail.com')),
       (600, (SELECT id FROM users WHERE username = 'petr@gmail.com')),
       (500, (SELECT id FROM users WHERE username = 'petr@gmail.com')),
       (400, (SELECT id FROM users WHERE username = 'sveta@gmail.com')),
       (300, (SELECT id FROM users WHERE username = 'sveta@gmail.com')),
       (500, (SELECT id FROM users WHERE username = 'vlad@gmail.com')),
       (700, (SELECT id FROM users WHERE username = 'vlad@gmail.com')),
       (340, (SELECT id FROM users WHERE username = 'vlad@gmail.com')),
       (440, (SELECT id FROM users WHERE username = 'kate@gmail.com')),
       (510, (SELECT id FROM users WHERE username = 'kate@gmail.com')),
       (630, (SELECT id FROM users WHERE username = 'kate@gmail.com'));

INSERT INTO chat (name)
VALUES ('dmdev'),
       ('java'),
       ('database');

INSERT INTO user_chat(user_id, chat_id)
VALUES ((SELECT id FROM users WHERE username = 'ivan@gmail.com'), (SELECT id FROM chat WHERE name = 'dmdev')),
       ((SELECT id FROM users WHERE username = 'petr@gmail.com'), (SELECT id FROM chat WHERE name = 'dmdev')),
       ((SELECT id FROM users WHERE username = 'sveta@gmail.com'), (SELECT id FROM chat WHERE name = 'dmdev')),
       ((SELECT id FROM users WHERE username = 'petr@gmail.com'), (SELECT id FROM chat WHERE name = 'java')),
       ((SELECT id FROM users WHERE username = 'sveta@gmail.com'), (SELECT id FROM chat WHERE name = 'java')),
       ((SELECT id FROM users WHERE username = 'vlad@gmail.com'), (SELECT id FROM chat WHERE name = 'java')),
       ((SELECT id FROM users WHERE username = 'kate@gmail.com'), (SELECT id FROM chat WHERE name = 'java')),
       ((SELECT id FROM users WHERE username = 'petr@gmail.com'), (SELECT id FROM chat WHERE name = 'database')),
       ((SELECT id FROM users WHERE username = 'kate@gmail.com'), (SELECT id FROM chat WHERE name = 'database'));