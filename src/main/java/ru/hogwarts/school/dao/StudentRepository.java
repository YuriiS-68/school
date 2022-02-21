package ru.hogwarts.school.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.SessionScope;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Repository
@SessionScope
public interface StudentRepository extends JpaRepository<Student, Long> {
    long deleteStudentById(Long id);
    Collection<Student> findStudentsByAgeBetween(Integer min, Integer max);
}
