package com.code.demo.service;

import com.code.demo.exception.StudentAlreadyExistsException;
import com.code.demo.exception.StudentNotFoundException;
import com.code.demo.model.Student;
import com.code.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student addStudent(Student student) {
        if(studentAlreadyExists(student.getEmail())){
            throw new StudentAlreadyExistsException(student.getEmail() + " already exists!");
        }

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student, Long id) {
        return studentRepository.findById(id)
                .map(st -> {
                    st.setFirstName(student.getFirstName());
                    st.setLastName(student.getLastName());
                    st.setEmail(student.getEmail());
                    st.setDepartment(student.getDepartment());
                    return studentRepository.save(st);
                }).orElseThrow(()-> new StudentNotFoundException("Student not found!"));
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException("Student not found!"));
    }

    @Override
    public void deleteStudent(Long id) {
        if(!studentRepository.existsById(id)){
            throw new StudentNotFoundException("Sorry, student not found");
        }
        studentRepository.deleteById(id);
    }

    private boolean studentAlreadyExists(String studentEmail){
        return studentRepository.findByEmail(studentEmail).isPresent();
    }

}
