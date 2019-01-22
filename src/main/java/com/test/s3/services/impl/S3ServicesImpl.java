package com.test.s3.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.test.s3.dao.FileAttachDao;
import com.test.s3.services.S3Services;
import com.test.s3.vo.FileAttachVo;
 
@Service
public class S3ServicesImpl implements S3Services {
	
	private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);
	
	@Autowired
	private AmazonS3 s3client;
	
	@Autowired
	private FileAttachDao fileAttachDao;
 
	@Value("${gkz.aws.access_key_id}")
	private String awsId;
 
	@Value("${gkz.aws.secret_access_key}")
	private String awsKey;
	
	@Value("${gkz.s3.region}")
	private String region;
	
	@Value("${gkz.s3.bucket}")
	private String bucketName;
 
	@Override
	public ByteArrayOutputStream downloadFile(String keyName) {
		try {
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
            
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            
            return baos;
		} catch (IOException ioe) {
			logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
        	logger.info("sCaught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
			throw ase;
        } catch (AmazonClientException ace) {
        	logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
            throw ace;
        }
		
		return null;
	}
 
	@Override
	public FileAttachVo uploadFile(String keyName, MultipartFile file) {
		FileAttachVo fileAttachVo = new FileAttachVo();
		
		try {
			logger.info("This AWS Access Key Id:    " + this.awsId);
			logger.info("This AWS Access Scret Key: " + this.awsKey);
			logger.info("This AWS S3 Bucket:        " + this.bucketName);
			logger.info("This AWS S3 Region:        " + this.region);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Random random = new Random();
			
			
			String streFileName = dateFormat.format(new Date()).toString() + "" + (random.nextInt(8999) + 1000) + "." + this.getExtension(keyName);
			String attachId = String.valueOf(System.nanoTime()); 
			String uploadPath = "edu/contents/" + streFileName;
			String orgNm = keyName;
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			s3client.putObject(bucketName, uploadPath, file.getInputStream(), metadata);
			logger.info("Success Upload");
			
			//DB attach관련 등록
			
			fileAttachVo.setReal_file_nm(orgNm);
			fileAttachVo.setSave_file_nm(streFileName);
			fileAttachVo.setAttach_id(attachId);
			fileAttachVo.setFile_path("/contents");
			fileAttachVo.setFile_size(file.getSize());
			
			fileAttachDao.insert(fileAttachVo);
			
			return fileAttachVo;
		} catch(IOException ioe) {
			logger.error("IOException: " + ioe.getMessage());
		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
			//throw ase;
			return fileAttachVo;
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
            //throw ace;
            return fileAttachVo;
        }
		return null;
	}
	
	public List<String> listFiles() {
		
	  ListObjectsRequest listObjectsRequest = 
              new ListObjectsRequest()
                    .withBucketName(bucketName);
                    //.withPrefix("test" + "/");
		
		List<String> keys = new ArrayList<>();
		
		ObjectListing objects = s3client.listObjects(listObjectsRequest);
		
		while (true) {
			List<S3ObjectSummary> summaries = objects.getObjectSummaries();
			if (summaries.size() < 1) {
				break;
			}
			
			for (S3ObjectSummary item : summaries) {
	            if (!item.getKey().endsWith("/"))
	            	keys.add(item.getKey());
	        }
			
			objects = s3client.listNextBatchOfObjects(objects);
		}
		
		return keys;
	}
	
	public String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	}
}
