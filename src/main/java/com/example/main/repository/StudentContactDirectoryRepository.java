package com.example.main.repository;



import org.springframework.data.mongodb.repository.MongoRepository;


import com.example.main.model.StudentContactDirectory;

public interface StudentContactDirectoryRepository extends MongoRepository<StudentContactDirectory, String> {
	 	 
	 
}
