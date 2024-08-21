package com.app.Controller;

import com.app.Model.Student;
import com.app.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("students")
    public List<Student> findAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("student/{id}")
    public ResponseEntity<EntityModel<Student>> getStudent(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);

        if (student.isPresent()) {
            Student studentObj = student.get();

            // Wrap the Student object in an EntityModel
            EntityModel<Student> entityModel = EntityModel.of(studentObj);

            // Add self-link
            entityModel.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(StudentController.class).getStudent(id)).withSelfRel());

            // Add link to all students
            entityModel.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(StudentController.class).findAllStudents()).withRel("all-students"));

            return ResponseEntity.ok(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("student/add")
    public void addStudent(@RequestBody Student student) {
        studentService.createStudent(student);
    }

    @PutMapping("student/update/{id}")
    public ResponseEntity<EntityModel<Student>> updateStudent(@RequestBody Student student, @PathVariable Long id) {
        return studentService.getStudentById(id).map(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setAge(student.getAge());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setCourse(student.getCourse());
            Student updatedStudent = studentService.updateStudent(existingStudent);

            // Wrap the updated student in an EntityModel and add links
            EntityModel<Student> entityModel = EntityModel.of(updatedStudent);

            // Add self-link
            entityModel.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(StudentController.class).getStudent(id)).withSelfRel());

            // Add link to all students
            entityModel.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(StudentController.class).findAllStudents()).withRel("all-students"));

            return ResponseEntity.ok(entityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("student/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);

        return student.map(s -> {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
