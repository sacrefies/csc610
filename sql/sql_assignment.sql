/*
Create SQL queries (select statements) that accomplish the following.
The schema (table definitions) for the database for this assignment are part of
the SQL lecture slides, which are the following:

Patient:        | pid(pk) | name | dob | addr | tel |

Exam:           | acc(pk) | pid(fk) | eid(fk) | result | date |

ExamDictionary: | eid(pk) | name | normal |
*/

-- List all of the Exams for Ms. Jones (pid=12)
SELECT ex.*
FROM exam AS e
JOIN patient p ON ex.pid = p.pid
WHERE p.pid = 12;

-- List all of the Patients that hail from jolly old London
SELECT p.*
FROM patient p
WHERE p.addr LIKE '%jolly old London%';

-- List all of the Exams with results that exceed their normal.
SELECT ex.*
FROM exam ex
JOIN examdictionary exd ON ex.eid = exd.eid
WHERE ex.result <> exd.normal;

-- List the name of all Patients that had Exams with results that exceed their
-- (the exam's) normal
SELECT p.name
FROM exam ex
JOIN patient p ON ex.pid = p.pid
JOIN examdictionary exd ON ex.eid = exd.eid
WHERE ex.result <> exd.normal;

-- List the name of all Patients that had Exams with results that exceed their
-- (the exam's) normal, but list names only once and in alphabetical order.
SELECT DISTINCT p.name
FROM exam ex
JOIN patient p ON p.pid = ex.pid
JOIN examdictionary exd ON exd.eid = ex.eid
WHERE ex.result <> exd.normal
