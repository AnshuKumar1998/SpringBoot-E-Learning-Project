package com.example.main.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



@Document(collection = "student_contact_directory")
public class StudentContactDirectory {
    @Id
    private String id;


    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only alphabetic characters")
    private String name;

    @NotNull(message = "Relation cannot be null")
    @NotBlank(message = "Relation cannot be empty")
    @Size(min = 2, max = 30, message = "Relation must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Relation must contain only alphabetic characters")
    private String relation;
    
    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    
    @NotNull(message = "Student ID cannot be null")
    @NotBlank(message = "Name cannot be empty")
    private String studentID;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentId) {
		studentID = studentId;
	}

	@Override
	public String toString() {
		return "StudentContactDirectory [id=" + id + ", name=" + name + ", relation=" + relation + ", mobileNumber="
				+ mobileNumber + ", StudentID=" + studentID + "]";
	}





}
