package com.example.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageProcessing {

	public static String convertImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }


	public static String base64ToImage(String base64String, String imageName, String email) {
	    try {
	        byte[] imageBytes = Base64.getDecoder().decode(base64String);


	        File directory = new File("src/main/resources/static/uploaded-student-data/" + email);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	        String outputPath = "src/main/resources/static/uploaded-student-data/"+ email +"/" + imageName;

	        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
	            fos.write(imageBytes);
	        }


	        return outputPath;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
