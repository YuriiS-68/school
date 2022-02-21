package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dao.FacultyRepository;
import ru.hogwarts.school.exception.AlreadyExistException;
import ru.hogwarts.school.exception.BedParamException;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        if (facultyRepository.existsFacultyByNameAndColor(faculty.getName(), faculty.getColor())){
            throw new AlreadyExistException("This faculty " + faculty.getName() + " already exist in DB");
        }
        facultyRepository.save(faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
        return faculty;
    }

    @Override
    public void deleteFaculty(Long id) {
        long numOfFacultyDeleted = facultyRepository.deleteFacultyById(id);
        if (numOfFacultyDeleted != 1){
            throw new NotFoundException("No such id " + id + " found in the DB");
        }
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        if (color == null){
            throw new BedParamException();
        }
        return getAllFaculties().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }

    public Collection<Faculty> findFacultyByNameOrColor(String input, String color) {
        return facultyRepository.findFacultyByNameOrColorIgnoreCase(input, color);
    }
}
