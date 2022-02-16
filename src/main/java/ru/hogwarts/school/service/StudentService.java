package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student addStudent(Student student);
    Student findStudent(Long id);
    Student updateStudent(Student student);
    void deleteStudent(Long id);
    Collection<Student> getAllStudents();
}
