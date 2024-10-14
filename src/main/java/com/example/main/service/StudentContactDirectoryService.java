package com.example.main.service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.main.model.StudentContactDirectory;
import com.example.main.repository.StudentContactDirectoryRepository;


import jakarta.validation.Valid;

@Service
public class StudentContactDirectoryService {
    @Autowired
    private StudentContactDirectoryRepository studentDirectoryRepository;

    public StudentContactDirectory createContactDirectory(@Valid StudentContactDirectory studentDirectory) {

        return studentDirectoryRepository.save(studentDirectory);
    }
    
    public Optional<StudentContactDirectory> findByID(String id) {
        return studentDirectoryRepository.findById(id); 
    }
    
   
}
