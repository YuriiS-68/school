package ru.hogwarts.school.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    long deleteStudentById(Long id);
}
