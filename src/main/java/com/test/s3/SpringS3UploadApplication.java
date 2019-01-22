package com.test.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:${aws.config}" })
public class SpringS3UploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringS3UploadApplication.class, args);
	}

}

