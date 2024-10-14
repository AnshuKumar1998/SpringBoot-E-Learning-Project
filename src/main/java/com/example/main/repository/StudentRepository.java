package com.example.main.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.main.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {

	 Student findByEmail(String email);
	 
	 
	 
}
