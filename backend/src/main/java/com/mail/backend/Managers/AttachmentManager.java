package com.mail.backend.Managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mail.backend.Utils.AttachmentUtils;

/* 
@RestController
@RequestMapping("/attachment")
public class AttachmentManager {

    @Autowired
    private AttachmentUtils AttachmentUtils;
    

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam Long id, @RequestParam String path,@RequestParam MultipartFile file){

        String fileName = AttachmentUtils.storeFile(AttachmentUtils.convertMFtoFile(file), id, path);

        return ResponseEntity.ok(fileName);
    }

}*/
