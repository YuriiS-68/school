package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dao.FacultyRepository;
import ru.hogwarts.school.exception.AlreadyExistException;
import ru.hogwarts.school.exception.BedParamException;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {
    private static final Logger log = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        log.info("Was invoked method add faculty: {}", faculty);
        if (facultyRepository.existsFacultyByNameAndColor(faculty.getName(), faculty.getColor())){
            String errorMessage = "This faculty " + faculty.getName() + " already exist in DB";
            log.error(errorMessage);
            throw new AlreadyExistException(errorMessage);
        }
        facultyRepository.save(faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(Long id) {
        log.info("Was invoked method find faculty: {}", id);
        return facultyRepository.findById(id).get();
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        log.info("Was invoked method update faculty: {}", faculty);
        facultyRepository.save(faculty);
        return faculty;
    }

    @Override
    public void deleteFaculty(Long id) {
        log.info("Was invoked method delete faculty: {}", id);
        long numOfFacultyDeleted = facultyRepository.deleteFacultyById(id);
        if (numOfFacultyDeleted != 1){
            String errorMessage = "No such id " + id + " found in the DB";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        log.info("Was invoked method get all faculties.");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        log.info("Was invoked method get faculty by color: {}", color);
        if (color == null){
            String errorMessage ="Color parameter missing.";
            log.error(errorMessage);
            throw new BedParamException();
        }
        return facultyRepository.findFacultyByColor(color);
    }

    public Collection<Faculty> findFacultyByNameOrColor(String name, String color) {
        log.info("Was invoked method get faculty by name: {} or color: {}", name, color);
        return facultyRepository.findFacultyByNameOrColorIgnoreCase(name, color);
    }
}
