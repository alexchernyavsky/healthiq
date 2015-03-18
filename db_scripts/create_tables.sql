
-- command line argument
-- when logged into psql editor
--\c healthiq to connect
-- \i create_tables.sql
-- to list all existing tables:
-- \dt

CREATE TABLE food_index
(
  id      BIGSERIAL PRIMARY KEY,
  food_name    VARCHAR(500) NOT NULL,
  glycemic_index   INTEGER NOT NULL
);

CREATE TABLE exercise_index
(
  id      BIGSERIAL PRIMARY KEY,
  exercise_name    VARCHAR(500) NOT NULL,
  exercise_index   INTEGER NOT NULL
);