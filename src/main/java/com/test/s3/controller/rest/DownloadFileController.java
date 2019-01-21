package com.test.s3.controller.rest;

import java.io.ByteArrayOutputStream;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
 
import com.test.s3.services.S3Services;
  
@RestController
public class DownloadFileController {
 	
	@Autowired
	S3Services s3Services;
	 	
    /*
     * Download Files
     */
	@GetMapping("/api/file/{keyname}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String keyname) {
		ByteArrayOutputStream downloadInputStream = s3Services.downloadFile(keyname);
	
		return ResponseEntity.ok()
					.contentType(contentType(keyname))
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + keyname + "\"")
					.body(downloadInputStream.toByteArray());	
	}
	
	/*
	 * List ALL Files
	 */
	@GetMapping("/api/file/all")
	public List<String> listAllFiles(){
		return s3Services.listFiles();
	}
	
	private MediaType contentType(String keyname) {
		String[] arr = keyname.split("\\.");
		String type = arr[arr.length-1];
		switch(type) {
			case "txt": return MediaType.TEXT_PLAIN;
			case "png": return MediaType.IMAGE_PNG;
			case "jpg": return MediaType.IMAGE_JPEG;
			default: return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}