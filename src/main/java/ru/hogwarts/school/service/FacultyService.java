package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Optional;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    Faculty updateFaculty(Faculty faculty);

    void deleteFaculty(Long id);

    Collection<Faculty> getAllFaculties();

    Optional<String> getLongestNameFaculty();

    Collection<Faculty> getFacultiesByColor(String color);

    Collection<Faculty> findFacultyByNameOrColor(String name, String color);
}
