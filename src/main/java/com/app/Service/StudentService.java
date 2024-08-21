package com.app.Service;

import com.app.Model.Student;
import com.app.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;


    public List<Student> getAllStudents(){
        return  studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id){
        return studentRepository.findById(id);
    }

    public void createStudent(Student student){
        studentRepository.save(student);
    }

    public Student updateStudent(Student student){
        return studentRepository.save(student);

    }

    public void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }
}
