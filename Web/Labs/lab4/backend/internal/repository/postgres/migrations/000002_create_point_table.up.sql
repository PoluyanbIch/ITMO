CREATE TABLE points (
    id      SERIAL PRIMARY KEY,
    x       FLOAT NOT NULL,
    y       FLOAT NOT NULL,
    r       FLOAT NOT NULL,
    isHit   BOOLEAN NOT NULL,
    userID  INTEGER NOT NULL
)