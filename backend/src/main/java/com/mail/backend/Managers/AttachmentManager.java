package com.mail.backend.Managers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    
    @PostMapping("/attachment/upload")
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

}
