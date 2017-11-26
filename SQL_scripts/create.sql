CREATE TABLE IF NOT EXISTS Student (
  email VARCHAR(50) PRIMARY KEY
  , password INT
  , first_name VARCHAR(25)
  , last_name VARCHAR(25)
  , gender CHAR)
;

CREATE TABLE IF NOT EXISTS Admin (
    email VARCHAR(50) PRIMARY KEY
  , password INT
  , first_name VARCHAR(25)
  , last_name VARCHAR(25))
;

CREATE TABLE IF NOT EXISTS Rating (
  instructor_id INT
  , student_email VARCHAR(50)
  , title VARCHAR(25)
  , score INT CHECK (score >= 0 AND score <= 5)
  , hotness INT CHECK (hotness >= 0 AND hotness <= 5)
  , comment VARCHAR(512)
  , date DATETIME
  , PRIMARY KEY (instructor_id, student_email)
);

CREATE TABLE IF NOT EXISTS Instructor (
  instructor_id INT PRIMARY KEY
  , first_name VARCHAR(25)
  , last_name VARCHAR(25)
  , gender CHAR
)