package com.example.main.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.helper.ImageProcessing;
import com.example.main.config.JwtUtil;
import com.example.main.dto.StudentDTO;
import com.example.main.model.Student;
import com.example.main.service.StudentService;
import com.example.main.handler.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3001")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Welcome to the Home Page");
        model.addAttribute("page", "home");
        return "index";
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
    	  model.addAttribute("title", "Registration");
        return "register";
    }



    @GetMapping("/login")
    public String loginPage(Model model) {
    	model.addAttribute("title", "Login");
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@Valid @ModelAttribute StudentDTO studentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            Student student = new Student();
            student.setName(studentDTO.getName());
            student.setEmail(studentDTO.getEmail());
            student.setMobileNumber(studentDTO.getMobileNumber());
            student.setGender(studentDTO.getGender());
            student.setDob(studentDTO.getDob());
            student.setAddress(studentDTO.getAddress());
            student.setPassword(studentDTO.getPassword());

            byte[] imageBytes = studentDTO.getImageFile().getBytes();
            String base64Image = ImageProcessing.convertImageToBase64(imageBytes);
            String originalFilename = studentDTO.getImageFile().getOriginalFilename();
            String fileName = originalFilename != null ? originalFilename : "unknown";
            String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

            Map<String, String> imageDetails = new HashMap<>();
            imageDetails.put("name", fileName.substring(0, fileName.lastIndexOf('.')));
            imageDetails.put("extension", fileExtension);
            imageDetails.put("base64code", base64Image);
            student.setImageDetails(imageDetails);



            studentService.register(student);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Student registered successfully.");
            System.out.println("Student = " + response);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/studentdata/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email;

        // Check for the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }

        try {
            // Extract email from the token
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Load userDetails from the database
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Validate the token
        if (jwtUtil.validateToken(token, userDetails)) {
            try {
                // Attempt to delete the student
                studentService.deleteById(id);
                return ResponseEntity.ok("Student deleted successfully.");
            } catch (ResourceNotFoundException e) {
                // Catch the exception and return the error message
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }




    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @ModelAttribute StudentDTO studentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            
            Student existingStudent = studentService.findByID(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            System.out.println("student = "+ existingStudent);
            
            existingStudent.setName(studentDTO.getName());
            existingStudent.setEmail(studentDTO.getEmail());
            existingStudent.setMobileNumber(studentDTO.getMobileNumber());
            existingStudent.setGender(studentDTO.getGender());
            existingStudent.setDob(studentDTO.getDob());
            existingStudent.setAddress(studentDTO.getAddress());


            if (studentDTO.getImageFile() != null && !studentDTO.getImageFile().isEmpty()) {
                byte[] imageBytes = studentDTO.getImageFile().getBytes();
                String base64Image = ImageProcessing.convertImageToBase64(imageBytes);
                String originalFilename = studentDTO.getImageFile().getOriginalFilename();
                String fileName = originalFilename != null ? originalFilename : "unknown";
                String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

                Map<String, String> imageDetails = new HashMap<>();
                imageDetails.put("name", fileName.substring(0, fileName.lastIndexOf('.')));
                imageDetails.put("extension", fileExtension);
                imageDetails.put("base64code", base64Image);
                existingStudent.setImageDetails(imageDetails);
            }

       
            studentService.update(existingStudent);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Student updated successfully.");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        try {
            Student student = studentService.login(email, password);

            if (student != null) {
                // Get role based on isTeacher
                String role = student.isTeacher() ? "ROLE_TEACHER" : "ROLE_USER"; // Update role here
                String token = jwtUtil.generateToken(student.getEmail(), role); // Pass the role
                System.out.println(role);
                Map<String, Object> response = new HashMap<>();
                response.put("ok", true);
                response.put("token", token);
                System.out.println(response);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(Map.of("ok", false, "message", "Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("ok", false, "message", "An error occurred"));
        }
    }


    @GetMapping("/studentprocessing")
    public String studentdataprocessing(Model model) {
    	model.addAttribute("title", "Welcome to the Dashboard");
        return "dashboard"; // This will return login.html page
    }


    @GetMapping("/studentdata")
    public ResponseEntity<?> getStudentData(HttpServletRequest request) {
    
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Extract the token

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }

        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Load userDetails from the database
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Validate token with the loaded userDetails
        if (jwtUtil.validateToken(token, userDetails)) {
            Student student = studentService.getStudentByEmail(email);

            if (student != null) {
          

                return ResponseEntity.ok(student);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/all-student-data-list")
    public ResponseEntity<?> getAllStudentData(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email;
        System.out.println("token");
        // Check for the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Extract the token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        System.out.println("token" + token);
        System.out.println("email" + email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // Validate the token (implementation not shown here)
        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Check user role (this part should be implemented)
        String role =jwtUtil.extractRole(token);
        if (!role.equals("ROLE_TEACHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }


    @GetMapping("/notice")
    public String noticePage(Model model) {
    	model.addAttribute("title", "Notice Page");
        return "notice";
    }
    
    @GetMapping("/noticeDataAll")
    public ResponseEntity<?> noticeDataAll(HttpServletRequest request) {
    
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Extract the token

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }

        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Load userDetails from the database
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Validate token with the loaded userDetails
        if (jwtUtil.validateToken(token, userDetails)) {
            Student student = studentService.getStudentByEmail(email);

            if (student != null) {
          

                return ResponseEntity.ok(student);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
    
    @GetMapping("/404")
    public String pagenotfound(Model model) {
    	model.addAttribute("title", "404 Not Found");
    	model.addAttribute("page", "page");
        return "404";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
    	 model.addAttribute("title", "About");
         model.addAttribute("page", "about");
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
    	model.addAttribute("title", "Contact");
        model.addAttribute("page", "contact");
        return "contact";
    }

    @GetMapping("/courses")
    public String coursesPage(Model model) {
    	 model.addAttribute("title", "Courses");
         model.addAttribute("page", "courses");
        return "courses";
    }

    @GetMapping("/team")
    public String teamPage(Model model) {
    	 model.addAttribute("title", "Services");
         model.addAttribute("page", "page");
        return "team";
    }

    @GetMapping("/testimonial")
    public String testimonialPage(Model model) {
    	 model.addAttribute("title", "Testimonial");
         model.addAttribute("page", "page");
        return "testimonial";
    }



}
