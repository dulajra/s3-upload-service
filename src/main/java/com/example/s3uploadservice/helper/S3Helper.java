package com.example.s3uploadservice.helper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.s3uploadservice.config.AWSConfigs;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Helper {

    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(AWSConfigs.REGION)
            .build();

    public void upload (String bucketName, String key, String filePath) {
        s3Client.putObject(bucketName, key, new File(filePath));
    }
}
