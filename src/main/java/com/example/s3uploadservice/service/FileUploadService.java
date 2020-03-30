package com.example.s3uploadservice.service;

import com.example.s3uploadservice.controller.FileUploadController;
import com.example.s3uploadservice.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    private final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

    private static final String TEMP_FILE_DIR = "/tmp/";
    private static final String OUTPUT_S3_NAME = "vidcloudoutput";

    private S3Service s3Service;

    @Autowired
    public FileUploadService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String handleFileUpload(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();
        String tempFilePath = TEMP_FILE_DIR + uuid.toString();
        File file = new File(tempFilePath);

        try {
            multipartFile.transferTo(file);

            List<String> labels = FileUtils.extractOutput(tempFilePath);
            StringBuilder resultLabelBuilder = new StringBuilder();

            for (int i = 0; i < labels.size(); i++) {
                resultLabelBuilder.append(labels.get(i));

                if (i < labels.size() - 1) {
                    resultLabelBuilder.append(",");
                }
            }

            String resultLabel = resultLabelBuilder.toString();

            if(resultLabel.isEmpty()){
                resultLabel = "no object detected";
            }

            String outputFilename = "(\"" + multipartFile.getOriginalFilename() + "\",\"" + resultLabel + "\")";
            s3Service.upload(OUTPUT_S3_NAME, outputFilename, file);
            return outputFilename;
        } catch (IOException e) {
            LOGGER.error("Error occurred while handling upload.", e);
            return null;
        }
    }

}