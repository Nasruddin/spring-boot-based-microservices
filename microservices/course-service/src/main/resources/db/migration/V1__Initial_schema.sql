DROP TABLE IF EXISTS course;
CREATE TABLE course (
      id                  BIGSERIAL PRIMARY KEY NOT NULL,
      author              varchar(255) NOT NULL,
      price               float8 NOT NULL,
      title               varchar(255) NOT NULL,
      publisher           varchar(255) NOT NULL,
      created_date        timestamp NOT NULL,
      last_modified_date  timestamp NOT NULL,
      version             integer NOT NULL
);