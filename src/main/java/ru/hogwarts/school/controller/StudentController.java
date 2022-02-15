package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentServiceImpl studentService;

    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id){
        Student student = studentService.findStudent(id);
        if (student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping("/add")
    public ResponseEntity<Student> createStudent(@RequestBody Student student){
        Student addStudent = studentService.addStudent(student);
        return ResponseEntity.created(URI.create("")).body(addStudent);
    }

    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student){
        Student foundStudent = studentService.updateStudent(student);
        if (student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/del/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }

    @GetMapping(value = "/age", params = {"age"})
    public Collection<Student> getStudentsByAge(@RequestParam(value = "age", required = false) Integer age){
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/get")
    public Collection<Student> getAll(){
        return studentService.getAllStudents();
    }
}
