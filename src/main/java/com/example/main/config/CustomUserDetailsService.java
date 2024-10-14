package com.example.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.main.model.Student;
import com.example.main.repository.StudentRepository; // Ensure you have a repository

@Service  // This makes it a Spring bean
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;  // Ensure this is correctly implemented

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email);

        if (student == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        String role = student.getEmail().endsWith("@learning.com") ? "ROLE_TEACHER" : "ROLE_USER";

        // Convert the Student to UserDetails (you may need a custom implementation)
        return org.springframework.security.core.userdetails.User
                .withUsername(student.getEmail())
                .password(student.getPassword())
                .authorities(role) // You can define roles as needed
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
