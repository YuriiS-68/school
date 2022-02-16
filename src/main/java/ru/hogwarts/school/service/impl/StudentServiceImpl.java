package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.BedParamException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final HashMap<Long, Student> students;
    private long lastId = 0;

    public StudentServiceImpl() {
        this.students = new HashMap<>();
    }

    @Override
    public Student addStudent(Student student) {
        student.setId(++lastId);
        if (!checkValidName(student)){
            throw new BedParamException("Not valid name");
        }
        students.put(lastId, student);
        return student;
    }

    @Override
    public Student findStudent(Long id) {
        return students.get(id);
    }

    @Override
    public Student updateStudent(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        if (!students.containsKey(id)){
            throw new BedParamException("Nothing found for this " + id);
        }
        students.remove(id);
    }

    @Override
    public Collection<Student> getAllStudents() {
        return students.values();
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
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
