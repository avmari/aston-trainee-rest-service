CREATE TABLE IF NOT EXISTS users
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    username VARCHAR(64) NOT NULL UNIQUE ,
    first_name VARCHAR(64),
    last_name VARCHAR(64)
);

-- drop table users cascade;
-- drop table payment cascade;
-- drop table chat cascade;
-- drop table user_chat cascade;

CREATE TABLE IF NOT EXISTS payment
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    amount INT NOT NULL ,
    user_id uuid NOT NULL REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS chat
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_chat
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY ,
    user_id uuid NOT NULL REFERENCES users (id),
    chat_id uuid NOT NULL REFERENCES chat (id),
    UNIQUE (user_id, chat_id)
);