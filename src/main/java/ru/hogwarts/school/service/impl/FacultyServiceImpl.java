package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.BedParamException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final HashMap<Long, Faculty> faculties;
    private long lastId = 0;

    public FacultyServiceImpl() {
        this.faculties = new HashMap<>();
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(Long id) {
        return faculties.get(id);
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public void deleteFaculty(Long id) {
        if (!faculties.containsKey(id)){
            throw new BedParamException("Nothing found for this " + id);
        }
        faculties.remove(id);
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        if (color == null){
            throw new BedParamException();
        }
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
