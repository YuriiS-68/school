package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dao.StudentRepository;
import ru.hogwarts.school.exception.BedParamException;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        log.info("Was invoked method add student: {}", student);

        if (!checkValidName(student)){
            String errorMessage = "Student with id " + student.getId() + " has not valid name.";
            log.error(errorMessage);
            throw new BedParamException(errorMessage);
        }
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student findStudent(Long id) {
        log.info("Was invoked method find student: {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(Student student) {
        log.info("Was invoked method update student: {}", student);
        studentRepository.save(student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Was invoked method delete student: {}", id);
        long numOfStudentDeleted = studentRepository.deleteStudentById(id);
        if (numOfStudentDeleted != 1){
            String errorMessage = "No such id " + id + " found in the DB";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public Collection<Student> getAllStudents() {
        log.info("Was invoked method get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAge(Integer age) {
        log.info("Was invoked method get students by age: {}", age);
        if (age == null){
            String errorMessage ="Age parameter missing.";
            log.error(errorMessage);
            throw new BedParamException();
        }
        return getAllStudents().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Collection<String> getStudentsByFirstSymbol(Character symbol){
        log.info("Was invoked method get students by first symbol: {}", symbol);
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .filter(student -> student.getName().startsWith(String.valueOf(symbol)))
                .map(student -> student.getName().toUpperCase())
                .collect(Collectors.toList());
    }

    public void getNameStudentsThreads(){
        List<String> nameStudents = getNameAllStudents();
        System.out.println(printNameStudentsFromList(nameStudents));

        for (int i = 0; i < nameStudents.size() && i < 3; i++) {
            printName(nameStudents.get(i) + ", ");
        }

        new Thread(() ->{
            System.out.println("\n" + Thread.currentThread().getName() + " first is created");
            for (int i = 3; i < nameStudents.size() && i < 6; i++) {
                printName(nameStudents.get(i) + ", ");
            }
        }).start();

        new Thread(() ->{
            System.out.println("\n" + Thread.currentThread().getName() + " second is created");
            for (int i = 6; i < nameStudents.size(); i++) {
                if (i == nameStudents.size() - 1){
                    printName(nameStudents.get(i) + ".\n");
                } else {
                    printName(nameStudents.get(i) + ", ");
                }
            }
        }).start();
    }

    public void getNameStudentsThreadsSync(){
        printNameStudentsSync(50L);
        printNameStudentsSync(51L);
        printNameStudentsSync(52L);

        new Thread(() -> {
            System.out.println("\n" + Thread.currentThread().getName() + " first is created");
            printNameStudentsSync(53L);
            printNameStudentsSync(5L);
            printNameStudentsSync(6L);
        }).start();

        new Thread(() -> {
            System.out.println("\n" + Thread.currentThread().getName() + " second is created");
            printNameStudentsSync(7L);
            printNameStudentsSync(8L);
            printNameStudentsSync(9L);
        }).start();
    }

    private List<String> getNameAllStudents(){
        assert studentRepository != null;
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    private StringBuilder printNameStudentsFromList(List<String> names){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 1){
                stringBuilder.append(names.get(i)).append(".").append("\n");
            } else {
                stringBuilder.append(names.get(i)).append(", ");
            }
        }
        return stringBuilder;
    }

    private void printName(String name){
        System.out.print(name);
    }

    private synchronized void printNameStudentsSync(Long id){
        System.out.println(studentRepository.findById(id).get().getName() + "; ");
    }

    public Long getAgeAllStudentsAverage(){
        double ageAverage = studentRepository.findAll().stream()
                .mapToDouble(Student::getAge).average().orElse(0);
        if (ageAverage == 0){
            log.warn("There are not students in DB.");
        }
        return Math.round(ageAverage);
    }

    public Collection<Student> getStudentsByAgeBetween(Integer min, Integer max){
        log.info("Was invoked method get students by age between: {} and {}", min, max);
        return studentRepository.findStudentsByAgeBetween(min, max);
    }

    public Integer getQuantityStudents() {
        log.info("Was invoked method get quantity students.");
        return studentRepository.getQuantityStudents();
    }

    public Integer getAgeAverage(){
        log.info("Was invoked method get age average.");
        return studentRepository.getAgeAverage();
    }

    public Collection<Student> getLastAddedStudent(int count){
        log.info("Was invoked method get last added students: {}", count);
        return studentRepository.getFiveLastStudents(count);
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
}
