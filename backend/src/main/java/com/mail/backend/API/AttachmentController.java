package com.mail.backend.API;

import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mail.backend.Managers.EmailManager;
import com.mail.backend.Managers.AttachmentManager;
import com.mail.backend.Managers.EmailCreator;
import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.multipart.MultipartFile;
import com.mail.backend.Models.Afile.Afile;
import com.mail.backend.Utils.AttachmentUtils;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class AttachmentController {
    
    @PostMapping("/attachment")
    public Afile uploadAttachment(@RequestBody MultipartFile[] files) {
        AttachmentManager attachmentManager = (AttachmentManager) ManagerFactory.getManager("AttachmentManager");
        return attachmentManager.uploadFiles(files);
    }

}
