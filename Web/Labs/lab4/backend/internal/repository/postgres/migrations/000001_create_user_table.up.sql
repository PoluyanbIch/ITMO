CREATE TABLE users (
    id          SERIAL PRIMARY KEY,
    login       VARCHAR(50) UNIQUE NOT NULL,
    password    VARCHAR(250) NOT NULL
)