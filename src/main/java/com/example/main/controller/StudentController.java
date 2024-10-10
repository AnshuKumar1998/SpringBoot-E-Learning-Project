package com.example.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.main.config.JwtUtil;
import com.example.main.model.Student;
import com.example.main.service.StudentService;

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
    public ResponseEntity<?> register(@Valid @RequestBody Student student) {
    	
    	
    	System.out.println(student);
    	
       // Student registeredStudent = studentService.register(student);
        //return ResponseEntity.ok(registeredStudent);
    	return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        System.out.println(email);
        System.out.println(password);
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
    	System.out.println("done0");
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
            	System.out.println("done");
            	
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
