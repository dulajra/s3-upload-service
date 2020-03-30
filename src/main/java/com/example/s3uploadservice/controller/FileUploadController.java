package com.example.s3uploadservice.controller;

import com.example.s3uploadservice.model.Response;
import com.example.s3uploadservice.service.FileUploadService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    private final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService storageService) {
        this.fileUploadService = storageService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Upload a file",
            notes = "Upload a file to S3",
            response = Response.class
    )
    public ResponseEntity<Response> handleFileUpload(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Upload request received. Filename: {}", file.getOriginalFilename());
        String outputFileName = fileUploadService.handleFileUpload(file);

        Response response = new Response();

        if (outputFileName != null) {
            response.setStatus(true);
            response.setFileName(outputFileName);
            LOGGER.info("Upload success. Output filename: {}", outputFileName);
        } else {
            response.setStatus(false);
            response.setFileName(null);
            LOGGER.error("Upload failed");
        }

        return ResponseEntity.ok().body(response);
    }
}