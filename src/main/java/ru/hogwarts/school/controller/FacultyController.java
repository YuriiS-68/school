package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyServiceImpl facultyService;

    public FacultyController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Faculty>> getFacultyInfo(@PathVariable Long id){
        Optional<Faculty> faculty = Optional.ofNullable(facultyService.findFaculty(id));
        if (faculty.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping("/add")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty){
        Faculty addFaculty = facultyService.addFaculty(faculty);
        return ResponseEntity.created(URI.create("")).body(addFaculty);
    }

    @PutMapping("/update")
    public ResponseEntity<Optional<Faculty>> updateFaculty(@RequestBody Faculty faculty){
        Optional<Faculty> foundFaculty = Optional.ofNullable(facultyService.updateFaculty(faculty));
        if (foundFaculty.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("/del/{id}")
    public void deleteFaculty(@PathVariable Long id){
        facultyService.deleteFaculty(id);
    }

    @GetMapping(value = "/color", params = {"color"})
    public Collection<Faculty> getFacultiesByColor(@RequestParam(value = "color", required = false) String color){
        return facultyService.getFacultiesByColor(color);
    }

    @GetMapping("/get")
    public Collection<Faculty> getAll(){
        return facultyService.getAllFaculties();
    }

    @GetMapping(value = "/find")
    public ResponseEntity<Collection<Faculty>> getFacultyByNameOrColor(@RequestParam (required = false) String name,
                                                                       @RequestParam (required = false) String color){
        return ResponseEntity.ok(facultyService.findFacultyByNameOrColor(name, color));
    }
}
