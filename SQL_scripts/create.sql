DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS Rating;
DROP TABLE IF EXISTS Instructor;

CREATE TABLE IF NOT EXISTS Student (
  email VARCHAR(50) PRIMARY KEY
  , password BINARY(40) NOT NULL
  , first_name VARCHAR(25) NOT NULL
  , last_name VARCHAR(25) NOT NULL
  , gender CHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Admin (
    email VARCHAR(50) PRIMARY KEY
  , password BINARY(40) NOT NULL
  , first_name VARCHAR(25) NOT NULL
  , last_name VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS Instructor (
    instructor_id INT PRIMARY KEY
  , first_name VARCHAR(25) NOT NULL
  , last_name VARCHAR(25) NOT NULL
  , gender CHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Rating (
  instructor_id INT
  , student_email VARCHAR(50)
  , title VARCHAR(25) NOT NULL
  , score INT CHECK (score > 0 AND score <= 5)
  , hotness INT CHECK (hotness > 0 AND hotness <= 5)
  , comment VARCHAR(512) NOT NULL
  , date DATETIME NOT NULL
  , PRIMARY KEY (instructor_id, student_email)
  , FOREIGN KEY fk_student_email(student_email)
    REFERENCES Student(email)
    ON UPDATE CASCADE
  , FOREIGN KEY fk_instructor_id(instructor_id)
    REFERENCES Instructor(instructor_id)
    ON UPDATE CASCADE
);