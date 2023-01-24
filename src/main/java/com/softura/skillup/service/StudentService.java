package com.softura.skillup.service;

import com.softura.skillup.entity.Student;
import com.softura.skillup.exception.RecordNotFoundException;
import com.softura.skillup.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public void createStudent(Student student)  {
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return getIfExistById(id);
    }

    protected Student getIfExistById(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public Student updatedStudent(Student student) {
        getIfExistById(student.getId());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = getIfExistById(id);
        studentRepository.delete(student);
    }
}
