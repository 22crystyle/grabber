CREATE TABLE post
(
    id      INTEGER     NOT NULL PRIMARY KEY,
    name    VARCHAR(30) NOT NULL UNIQUE,
    text    VARCHAR(30),
    link    VARCHAR(30) UNIQUE,
    created BIGINT
)