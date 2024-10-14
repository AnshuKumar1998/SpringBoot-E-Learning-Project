package com.example.main.service;


import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.helper.DateAndTimeProcessing;
import com.example.helper.ImageProcessing;
import com.example.main.handler.ResourceNotFoundException;
import com.example.main.model.Student;
import com.example.main.repository.StudentRepository;

import jakarta.validation.Valid;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student register(@Valid Student student) {


    	 long unixTimestamp = DateAndTimeProcessing.convertToUnixTimestamp(student.getDob());
    	    student.setDob(String.valueOf(unixTimestamp));

    	    student.setPassword(BCrypt.hashpw(student.getPassword(), BCrypt.gensalt()));


        return studentRepository.save(student);
    }
    
    public Optional<Student> findByID(String id) {
        return studentRepository.findById(id); 
    }
    
    public Student update(@Valid Student student) {
    
     
        long unixTimestamp = DateAndTimeProcessing.convertToUnixTimestamp(student.getDob());
        student.setDob(String.valueOf(unixTimestamp));


        return studentRepository.save(student);
    }


    public Student login(String email, String password) {

        Student student = studentRepository.findByEmail(email);


        if (student != null && BCrypt.checkpw(password, student.getPassword())) {


        	  String folderPath = "src/main/resources/static/uploaded-student-data/" + email;
              File directory = new File(folderPath);
              if (!directory.exists()) {
                  directory.mkdirs();
              }

            return student;
        }
        throw new RuntimeException("Invalid Credentials");
    }

	public Student getStudentByEmail(String email) {

		 Student student = studentRepository.findByEmail(email);


		    if (student != null) {
		    	String imageName="";
		        long unixTimestamp = Long.parseLong(student.getDob());
		        LocalDate dateOfBirth = DateAndTimeProcessing.unixToDate(unixTimestamp);
		        student.setDob(dateOfBirth.toString());

		        Map<String, String> fileDetails = student.getImageDetails();
		        String name = fileDetails.get("name");
		        String extension = fileDetails.get("extension");
		        String base64Image = fileDetails.get("base64code");
		        imageName = name + extension;
		        String imagePath = ImageProcessing.base64ToImage(base64Image, imageName, email);
		        student.setImageBase64(imagePath);
		        student.setImageDetails(null);
		    }


		    return student;
	}

	public List<Student> getAllStudents() {
	    List<Student> students = studentRepository.findAll();

	    for (Student student : students) {

	        long unixTimestamp = Long.parseLong(student.getDob());
	        LocalDate dateOfBirth = DateAndTimeProcessing.unixToDate(unixTimestamp);
	        student.setDob(dateOfBirth.toString());


	        Map<String, String> fileDetails = student.getImageDetails();
	        if (fileDetails != null) {
	            String name = fileDetails.get("name");
	            String extension = fileDetails.get("extension");
	            String base64Image = fileDetails.get("base64code");
	            String imageName = name + extension;


	            String email = student.getEmail();
	            String imagePath = ImageProcessing.base64ToImage(base64Image, imageName, email);
	            student.setImageBase64(imagePath);
	            student.setImageDetails(null);
	        }
	    }

	    return students;
	}
	
	public void deleteById(String id) {
      
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.delete(student.get());
        } else {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
    }

}
