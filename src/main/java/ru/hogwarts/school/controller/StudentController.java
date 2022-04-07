package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/age", params = {"age"})
    public Collection<Student> getStudentsByAge(@RequestParam(value = "age") Integer age){
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/get")
    public Collection<Student> getAll(){
        return studentService.getAllStudents();
    }

    @GetMapping("/ages")
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(@RequestParam Integer min, @RequestParam Integer max){
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    @GetMapping(value = "/symbol", params = {"symbol"})
    public ResponseEntity<Collection<String>> getStudentsByFirstSymbol(@RequestParam(value = "symbol") Character symbol){
        Collection<String> namesStudents = studentService.getStudentsByFirstSymbol(symbol);
        if (namesStudents.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(namesStudents);
    }

    @GetMapping("/quantity-students")
    public Integer getQuantityStudentsSchool(){
        return studentService.getQuantityStudents();
    }

    @GetMapping("/avg")
    public Integer getAgeStudentsAverage(){
        return studentService.getAgeAverage();
    }

    @GetMapping("/avg-stream")
    public Long getAgeAllStudentsAverage(){
        return studentService.getAgeAllStudentsAverage();
    }

    @GetMapping("/last/{count}")
    public Collection<Student> getLastAddedStudents(@PathVariable int count){
        return studentService.getLastAddedStudent(count);
    }
}
