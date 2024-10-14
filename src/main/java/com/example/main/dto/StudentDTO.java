package com.example.main.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentDTO {

	@NotNull(message = "Name cannot be null")
	@NotBlank(message = "Name cannot be empty")
	@Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only alphabetic characters and spaces")
	private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotNull(message = "Gender cannot be null")
    @Pattern(regexp = "^(male|female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotNull(message = "Date of birth cannot be null")
    private String dob;

    @NotNull(message = "Address cannot be null")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Image cannot be null")
    private MultipartFile imageFile;


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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	 @Override
		public String toString() {
			return "StudentDTO [name=" + name + ", email=" + email + ", mobileNumber=" + mobileNumber + ", gender=" + gender
					+ ", dob=" + dob + ", address=" + address + ", password=" + password + ", imageFile=" + imageFile + "]";
		}




    // Getters and Setters
}
