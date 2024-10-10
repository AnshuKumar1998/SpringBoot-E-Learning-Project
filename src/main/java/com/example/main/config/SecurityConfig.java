package com.example.main.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/students/login", 
                		"/api/students/register", 
                		"/api/students/", 
                		"/api/students/studentprocessing",
                		"/api/students/contact",
                		"/api/students/about",
                		"/api/students/courses",
                		"/api/students/404",
                		"/api/students/team",
                		"/api/students/testimonial",
                		 "/css/**",   
                         "/js/**",     
                         "/img/**", 
                         "/lib/**", 
                         "/scss/**",
                         "/assets/**" 
                         
                		).permitAll()
                .requestMatchers("/api/students/dashboard","/api/students/studentprocessing").hasRole("TEACHER") 
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    
}
