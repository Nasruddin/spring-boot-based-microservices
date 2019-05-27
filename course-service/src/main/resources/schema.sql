DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS student_course;
DROP TABLE IF EXISTS seats;

CREATE TABLE courses (
  course_id     VARCHAR(100) NOT NULL,
  course_name   TEXT NOT NULL,
  course_desc TEXT NOT NULL,
  duration BIGINT NOT NULL,
  type TEXT NOT NULL,
  total_seats BIGINT NOT NULL,
  available_seats BIGINT NOT NULL,
  expired BOOLEAN NOT NULL,
  featured BOOLEAN NOT NULL,
  trending BOOLEAN NOT NULL,
  primary key(course_id)

);

CREATE TABLE students (
  student_id     VARCHAR(100) NOT NULL,
  student_name   TEXT NOT NULL,
  username   TEXT NOT NULL,
  department    TEXT NOT NULL,
  semster    TEXT NOT NULL,
  join_in    TEXT NOT NULL,
  primary key(student_id)
);

CREATE TABLE student_course (
  id  BIGINT auto_increment NOT NULL,
  username   VARCHAR(100) NOT NULL,
  course_id     VARCHAR(100) NOT NULL,
  status TEXT DEFAULT 'PENDING' NOT NULL,
  primary key(username, course_id)
);

CREATE TABLE seats (
  seat_id  BIGINT auto_increment NOT NULL,
  available BOOLEAN NOT NULL,
  type TEXT NOT NULL,
  course_id VARCHAR(100) NOT NULL,
  primary key(seat_id)
)