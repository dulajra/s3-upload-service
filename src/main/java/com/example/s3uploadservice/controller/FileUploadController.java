package com.example.s3uploadservice.controller;

import com.example.s3uploadservice.Response;
import com.example.s3uploadservice.service.FileUploadService;
import io.swagger.annotations.ApiOperation;
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
        String fileName = fileUploadService.handleFileUpload(file);

        Response response = new Response();

        if (fileName != null) {
            response.setStatus(true);
            response.setFileName(fileName);
        } else {
            response.setStatus(false);
            response.setFileName(null);
        }

        return ResponseEntity.ok().body(response);
    }
}