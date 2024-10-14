package com.example.main.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



@Document(collection = "students")
public class Student {
    @Id
    private String id;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private boolean isTeacher;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only alphabetic characters")
    private String name;

    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotNull(message = "Gender cannot be null")
    @Pattern(regexp = "^(male|female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotNull(message = "Address cannot be null")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @NotNull(message = "Date of birth cannot be null")
    private String dob;

    @Transient
    @NotBlank(message = "Image cannot be empty")
    private String imageBase64;

    private Map<String, String> imageDetails;




    public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Map<String, String> getImageDetails() {
		return imageDetails;
	}

	public void setImageDetails(Map<String, String> imageDetails) {
		this.imageDetails = imageDetails;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public String getImageBase64() {
		return imageBase64;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTeacher() { // Getter for isTeacher
        return isTeacher;
    }

    public void setTeacher(boolean isTeacher) { // Setter for isTeacher
        this.isTeacher = isTeacher;
    }

    @Override
    public String toString() {
        return "Student{id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", isTeacher='" + isTeacher + '\'' +
               ", mobileNo='" + mobileNumber + '\'' +
               ", gender='" + gender + '\'' +
               ", address='" + address + '\'' +
               ", dob='" + dob + '\'' +
               ", image='" + imageBase64 + '\'' +
               ", imageDetails='" + imageDetails + '\'' +

               '}';
    }
}
