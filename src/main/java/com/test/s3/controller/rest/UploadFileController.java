package com.test.s3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
 
import com.test.s3.services.S3Services;
 
@RestController
public class UploadFileController {
	
	@Autowired
	S3Services s3Services;
	
	@CrossOrigin("*")
    @PostMapping("/api/file/upload")
    public String uploadMultipartFile(@RequestParam("uploadfile") MultipartFile file) {
    	String keyName = file.getOriginalFilename();
		s3Services.uploadFile(keyName, file);
		return "Upload Successfully -> KeyName = " + keyName;
    }    
}