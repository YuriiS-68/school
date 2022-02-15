package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty addFaculty(Faculty faculty);
    Faculty findFaculty(Long id);
    Faculty updateFaculty(Faculty faculty);
    void deleteFaculty(Long id);
    Collection<Faculty> getAllFaculties();
}
