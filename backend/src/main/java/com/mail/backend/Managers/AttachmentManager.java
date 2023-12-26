package com.mail.backend.Managers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mail.backend.Models.Attachment.Attachment;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Utils.AttachmentUtils;


@RestController
@RequestMapping("/attachment")
public class AttachmentManager {

    @Autowired
    private AttachmentUtils AttachmentUtils;
    
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("id") int id, @RequestParam("path") String path, @RequestParam("files") MultipartFile[] files){
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<Attachment> attachments = new ArrayList<>();
        EmailManager emails = EmailManager.getInstance();
        Email email = emails.get(id);
        int i = 0;
        for(MultipartFile file  : files){
            String fileName = AttachmentUtils.storeFile(AttachmentUtils.convertMFtoFile(file), id, path);
            fileNames.add(fileName);
        //Adding file names to attachment in email
            if(i<attachments.size()){
                attachments.get(i).setPathLocation(fileName);
                i++;
            }
        }
        if(email != null){
            email.setAttachments(attachments);
            emails.saveEmails();
        }
    
        return ResponseEntity.ok(fileNames);
    }


    @GetMapping("/get/{file}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String file){

        Resource resource = AttachmentUtils.getFile(file);
        try{
            return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; file name=\\" + resource.getFilename() +"\\")
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .body(resource);
        
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

}
