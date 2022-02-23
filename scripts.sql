select * from student where age BETWEEN 18 AND 21;
select name from student;
select * from student WHERE name LIKE '%t%';
select * from student WHERE age < student.id;
select * from student GROUP BY id, age, name;