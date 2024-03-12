CREATE table customer
(
    id    BIGSERIAL PRIMARY KEY,
    name  TEXT   NOT NULL,
    email TEXT   NOT NULL UNIQUE,
    password TEXT   NOT NULL ,
    age   INTEGER NOT NULL,
    gender TEXT NOT NULL
)