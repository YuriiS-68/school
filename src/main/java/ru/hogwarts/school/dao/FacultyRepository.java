package ru.hogwarts.school.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.SessionScope;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

@Repository
@SessionScope
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    long deleteFacultyById(Long id);
    boolean existsFacultyByNameAndColor(String name, String color);
    Collection<Faculty> findFacultyByNameOrColorIgnoreCase(String input, String color);
}
