package com.test.s3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
 
import com.test.s3.services.S3Services;
import com.test.s3.vo.FileAttachVo;
 
@RestController
public class UploadFileController {
	
	@Autowired
	S3Services s3Services;
	
	@CrossOrigin("*")
    @PostMapping("/api/file/upload")
    public String uploadMultipartFile(@RequestParam("uploadfile") MultipartFile file) {
    	String keyName = file.getOriginalFilename();
    	FileAttachVo fileVo = s3Services.uploadFile(keyName, file);
		return fileVo.getAttach_id();
    }    
}