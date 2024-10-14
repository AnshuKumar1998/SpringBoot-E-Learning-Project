package com.example.main.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.example.main.dto.StudentContactDirectoryDTO;
import com.example.main.model.StudentContactDirectory;
import com.example.main.service.StudentContactDirectoryService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/api/studentDirectory")
public class StudentContactDirectoryController {

    @Autowired
    private StudentContactDirectoryService studentContactDirectory;


    @PostMapping("/createContact")
    public ResponseEntity<?> createStudentContact(@Valid @ModelAttribute StudentContactDirectoryDTO studentDirectoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            StudentContactDirectory student = new StudentContactDirectory();
            student.setName(studentDirectoryDTO.getName());
            student.setRelation(studentDirectoryDTO.getRelation());
            student.setMobileNumber(studentDirectoryDTO.getMobileNumber());
            student.setStudentID(studentDirectoryDTO.getStudentID());

            studentContactDirectory.createContactDirectory(student);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Student registered successfully.");
            System.out.println("Student = " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}
