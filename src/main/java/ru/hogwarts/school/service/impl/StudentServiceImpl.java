package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.dao.StudentRepository;
import ru.hogwarts.school.exception.BedParamException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        if (!checkValidName(student)){
            throw new BedParamException("Not valid name");
        }
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student findStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student updateStudent(Student student) {
        studentRepository.save(student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    private boolean checkValidName(Student student){
        char[] chars = student.getName().toCharArray();
        for (Character ch : chars){
            if (!Character.isLetter(ch)){
                return false;
            }
        }
        return true;
    }

    public Collection<Student> getStudentsByAge(Integer age) {
        if (age == null){
            throw new BedParamException();
        }
        return getAllStudents().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
