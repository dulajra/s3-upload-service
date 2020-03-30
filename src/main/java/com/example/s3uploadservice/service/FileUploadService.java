package com.example.s3uploadservice.service;

import com.example.s3uploadservice.helper.S3Helper;
import com.example.s3uploadservice.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileUploadService {

    private static final String TEMP_FILE_DIR = "/tmp/";

    private S3Helper s3Helper;

    @Autowired
    public FileUploadService(S3Helper s3Helper) {
        this.s3Helper = s3Helper;
    }

    public String handleFileUpload(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();
        String tempFileName = uuid.toString();
        String tempFilePath = TEMP_FILE_DIR + tempFileName;
        File f1 = new File(tempFilePath);

        try {
            multipartFile.transferTo(f1);
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
            s3Helper.upload("vidcloudoutput", outputFilename, tempFilePath);
            return outputFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}