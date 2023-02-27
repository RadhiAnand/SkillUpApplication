package com.softura.skillup.controller;

import com.softura.skillup.entity.Student;
import com.softura.skillup.exception.ScheduleDayException;
import com.softura.skillup.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;


    @GetMapping("/{id}")
    public ResponseEntity<DayOfWeek> getStudentById(@PathVariable Long id) throws ParseException {
        return new ResponseEntity<>(studentService.getScheduledDay(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> getStudents() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<DayOfWeek> getScheduledDay() throws ParseException, ScheduleDayException {
//        return new ResponseEntity<>(studentService.getScheduledDay(), HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<HttpStatus> createStudent(@RequestBody @Valid Student student)  {
        studentService.createStudent(student);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student) throws Exception, ScheduleDayException {
        return new ResponseEntity<>(studentService.updatedStudent(student), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable Long id)  {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
