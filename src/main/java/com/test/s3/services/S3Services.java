package com.test.s3.services;

import java.io.ByteArrayOutputStream;
import java.util.List;
 
import org.springframework.web.multipart.MultipartFile;

import com.test.s3.vo.FileAttachVo;
 
public interface S3Services {
	public ByteArrayOutputStream downloadFile(String keyName);
	public FileAttachVo uploadFile(String keyName, MultipartFile file);
	public List<String> listFiles();
}