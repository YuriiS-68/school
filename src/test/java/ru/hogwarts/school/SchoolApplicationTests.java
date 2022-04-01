package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.h2.tools.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.sql.SQLException;
import java.util.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

    private final String name = "Tom";
    private final String name1 = "Piter";
    private final String name2 = "Tod";
    private final String name3 = "Monika";
    private final String name4 = "Bob";

    private final int age = 27;
    private final int age18 = 18;
    private final int age22 = 22;
    private final int age28 = 28;
    private final int age30 = 30;


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Test
    public void createStudentTest(){
        Student student = givenStudentWith(name, age);

        ResponseEntity<Student> response = whenSendingCreateStudentRequest(student);
        thenStudentHasBeenCreated(response);
    }

    @Test
    public void getStudentInfoTest(){
        Student student = givenStudentWith(name, age);

        ResponseEntity<Student> createResponse = whenSendingCreateStudentRequest(student);
        thenStudentHasBeenCreated(createResponse);

        Student createStudent = createResponse.getBody();
        assert createStudent != null;
        thenStudentWithIdHasBeenFound(createStudent.getId(), createStudent);
    }

    @Test
    public void getStudentsByAgeTest(){
        Student student18 = givenStudentWith(name1, age18);
        Student student22 = givenStudentWith(name2, age22);
        Student student28 = givenStudentWith(name3, age28);
        Student student30 = givenStudentWith(name4, age30);

        whenSendingCreateStudentRequest(student18);
        whenSendingCreateStudentRequest(student22);
        whenSendingCreateStudentRequest(student28);
        whenSendingCreateStudentRequest(student30);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("age", "28");
        URI uri = getUriBuilder().path("/age").queryParams(queryParams).build().toUri();
        thenStudentsFoundByCriteria(uri, student28);
    }

    @Test
    public void getStudentsByAgeBetweenTest(){
        Student student18 = givenStudentWith(name1, age18);
        Student student22 = givenStudentWith(name2, age22);
        Student student28 = givenStudentWith(name3, age28);
        Student student30 = givenStudentWith(name4, age30);

        whenSendingCreateStudentRequest(student18);
        whenSendingCreateStudentRequest(student22);
        whenSendingCreateStudentRequest(student28);
        whenSendingCreateStudentRequest(student30);


        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("min", "20");
        queryParams.add("max", "29");
        URI uri = getUriBuilder().path("/ages").queryParams(queryParams).build().toUri();
        thenStudentsFoundByCriteria(uri, student22, student28);
    }

    @Test
    public void updateStudentTest(){
        Student student = givenStudentWith(name, age);

        ResponseEntity<Student> responseEntity = whenSendingCreateStudentRequest(student);
        thenStudentHasBeenCreated(responseEntity);
        Student createdStudent = responseEntity.getBody();

        assert createdStudent != null;
        whenUpdatingStudent(createdStudent);
        thenStudentHasBeenUpdated(createdStudent);
    }

    @Test
    public void deleteStudentTest(){
        Student student = givenStudentWith(name, age);

        ResponseEntity<Student> responseEntity = whenSendingCreateStudentRequest(student);
        thenStudentHasBeenCreated(responseEntity);
        Student createdStudent = responseEntity.getBody();

        assert createdStudent != null;
        whenDeletingStudent(createdStudent);
        thenStudentNotFound(createdStudent);
    }

    private void whenDeletingStudent(Student createdStudent){
        restTemplate.delete(getUriBuilder().path("/del/{id}").buildAndExpand(createdStudent.getId()).toUri());
    }

    private void whenUpdatingStudent(Student createdStudent){
        createdStudent.setAge(28);
        createdStudent.setName("Monika");

        restTemplate.put(getUriBuilder().path("/update").toUriString(), createdStudent);
    }

    private ResponseEntity<Student> whenSendingCreateStudentRequest(Student student){
        URI uri = URI.create(getUriBuilder().path("/add").toUriString());
        return restTemplate.postForEntity(uri, student, Student.class);
    }

    private void thenStudentNotFound(Student createdStudent){
        URI getUri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> emptyResponse = restTemplate.getForEntity(getUri, Student.class);

        Assertions.assertThat(emptyResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void thenStudentHasBeenCreated(ResponseEntity<Student> response){
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

    private void thenStudentHasBeenUpdated(Student createdStudent){
        URI getUri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> updateStudentResponse = restTemplate.getForEntity(getUri, Student.class);

        Assertions.assertThat(updateStudentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(updateStudentResponse.getBody()).isNotNull();
        Assertions.assertThat(updateStudentResponse.getBody().getAge()).isEqualTo(28);
        Assertions.assertThat(updateStudentResponse.getBody().getName()).isEqualTo("Monika");
    }

    private void thenStudentWithIdHasBeenFound(Long studentId, Student student){
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(studentId).toUri();
        ResponseEntity<Student> response = restTemplate.getForEntity(uri, Student.class);

        Assertions.assertThat(response.getBody()).isEqualTo(student);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void thenStudentsFoundByCriteria(URI uri, Student... students){

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Collection<Student> actualResult = response.getBody();
        resetIds(actualResult);
        Assertions.assertThat(actualResult).containsExactlyInAnyOrder(students);
    }

    private Student givenStudentWith(String name, int age){
        return new Student(name, age);
    }

    private void resetIds(Collection<Student> students){
        students.forEach(item -> item.setId(null));
    }

    private UriComponentsBuilder getUriBuilder(){
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/student");
    }
}
