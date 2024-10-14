package com.example.main.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentContactDirectoryDTO {

	@NotNull(message = "Name cannot be null")
	@NotBlank(message = "Name cannot be empty")
	@Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only alphabetic characters and spaces")
	private String name;
	
	@NotNull(message = "Relation cannot be null")
	@NotBlank(message = " Relation cannot be empty")
	@Size(min = 2, max = 30, message = "Relation must be between 2 and 30 characters")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Relation must contain only alphabetic characters and spaces")
	private String relation;
	
	@NotNull(message = "Student ID cannot be null")
	@NotBlank(message = " Student ID cannot be empty")
	private String studentID;


    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;


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


	public String getStudentID() {
		return studentID;
	}


	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	@Override
	public String toString() {
		return "StudentContactDirectoryDTO [name=" + name + ", relation=" + relation + ", studentID=" + studentID
				+ ", mobileNumber=" + mobileNumber + "]";
	}
    
    

}
