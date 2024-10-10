package com.example.main.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.main.model.Student;
import com.example.main.repository.StudentRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student register(Student student) {
    	
         
        student.setPassword(BCrypt.hashpw(student.getPassword(), BCrypt.gensalt()));
        return studentRepository.save(student);
    }

    public Student login(String email, String password) {
   
        Student student = studentRepository.findByEmail(email);
        
        
        if (student != null && BCrypt.checkpw(password, student.getPassword())) {
        	System.out.println("done : "+ student);
            return student;
        }
        throw new RuntimeException("Invalid Credentials");
    }

	public Student getStudentByEmail(String email) {
		 return studentRepository.findByEmail(email);
	}
	
	  public List<Student> getAllStudents() {
	        return studentRepository.findAll();
	    }
}
