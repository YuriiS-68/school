package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {

    Student addStudent(Student student);

    Student findStudent(Long id);

    Student updateStudent(Student student);

    void deleteStudent(Long id);

    Collection<Student> getAllStudents();

    Collection<Student> getStudentsByAge(Integer age);

    Collection<String> getStudentsByFirstSymbol(Character symbol);

    Long getAgeAllStudentsAverage();

    Collection<Student> getStudentsByAgeBetween(Integer min, Integer max);

    Integer getQuantityStudents();

    Integer getAgeAverage();

    Collection<Student> getLastAddedStudent(int count);
}
