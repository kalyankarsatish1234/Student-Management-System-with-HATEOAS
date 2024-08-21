package com.app.Controller;

import com.app.Model.Course;
import com.app.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("courses")
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GetMapping("course/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id){
        Optional<Course> course=courseService.getCourseById(id);
        return course.map(course1->ResponseEntity.ok().body(course1)).orElse(ResponseEntity.notFound().build());

    }
    @PostMapping("course/add")
    public Course addCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }

    @DeleteMapping("course/delete/{id}")
    public void deleteCourse(@PathVariable Long id){
        Optional<Course> course=courseService.getCourseById(id);
        course.map(c->{
                courseService.deleteCourse(id);
                return ResponseEntity.ok().build();
            }).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("course/update/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id,@RequestBody Course course){
        Optional<Course> course1=courseService.getCourseById(id);
        return course1.map(c->{
            c.setCourseName(c.getCourseName());
            c.setDescription(c.getDescription());
            Course updatedCourse=courseService.updateCourse(c);
            return ResponseEntity.ok(updatedCourse);
        }).orElseGet(()->ResponseEntity.notFound().build());

    }






}
