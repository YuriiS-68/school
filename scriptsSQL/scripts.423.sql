SELECT student.age, student.name, faculty.color, faculty.name
FROM student
INNER JOIN faculty ON student.faculty_id = faculty.id;

SELECT student.* FROM avatar INNER JOIN student ON student.id = avatar.student_id;