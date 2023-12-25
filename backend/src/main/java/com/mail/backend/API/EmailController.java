package com.mail.backend.API;

import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mail.backend.Managers.EmailManager;
import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;


@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class EmailController {

    @PostMapping("/emails")
    public Email create(@RequestHeader String authorization,@RequestBody Email email){
        User user=Auth.getUser(authorization);

        if(email.getBody()==null || email.getSubject() ==null || email.getTo()==null || user ==null){
            return null;
        }
        // TODO: handle draft
        
        // add default fields
        email.setFromUserId(user.getUsername());
        email.setSendDate(new Date());

        // create email
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        emailManager.add(email);

        // send it to inbox folder (hope to be asynchronous)



        // add it to sent folder
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder outBoxFolder = folderManager.getUserFolderByName( user.getUsername(),"Sent");
        folderManager.addEmail(outBoxFolder.getId(), email.getId());

        return email;
    }
    
    @GetMapping("/emails/{id}")
    public Email read(@RequestHeader String authorization,@PathVariable("id") Integer id){
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(id);
        User user= Auth.getUser(authorization);
        if(email.getFromUserId().equals(user.getUsername())){
            return email;
        }
        // if in toList also return email
        return null;
    }
}
