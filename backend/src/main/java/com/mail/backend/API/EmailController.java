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
import com.mail.backend.Managers.EmailCreator;
import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class EmailController {

    @PostMapping("/emails")
    public Email create(@RequestHeader String authorization, @RequestBody Email email) {
        User user = Auth.getUser(authorization);
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");

        if (email.getBody() == null || email.getSubject() == null || email.getTo() == null || user == null) {
            return null;
        }
        // TODO: handle draft
        System.out.println("DRAFTTTTTTTTTTT" + email.isDraft());
        if (email.isDraft()) {
            email.setFromUserId(user.getUsername());
            email.setFolderId(folderManager.getUserFolderByName(user.getUsername(), "Draft").getId());
            email.setSendDate(new Date());
            email = emailManager.add(email);
            folderManager.addEmail(email.getFolderId(), email.getId());
            return email;

        } else {

            return EmailCreator.createAndSend(email, user);
        }

    }

    @GetMapping("/emails/{id}")
    public Email read(@RequestHeader String authorization, @PathVariable("id") Integer id) {
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(id);
        User user = Auth.getUser(authorization);
        if (email.getFromUserId().equals(user.getUsername())) {
            return email;
        }
        // if in toList also return email
        return null;
    }

    @PutMapping("/emails")
    public Email update(@RequestHeader String authorization, @RequestBody Email email) {
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        User user = Auth.getUser(authorization);
        if (email.getFromUserId().equals(user.getUsername())) {
            emailManager.updateEmail(email.getId(), email.readEmail());
        }
        return null;
    }

    @DeleteMapping("/emails/{id}")
    public void delete(@RequestHeader String authorization, @PathVariable("id") Integer id) {
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        User user = Auth.getUser(authorization);
        Email email = emailManager.get(id);
        if (email == null) {
            return;
        }
        if (email.getFromUserId().equals(user.getUsername())) {
            // remove from its folder
            FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
            folderManager.removeEmail(email.getFolderId(), email.getId());
            emailManager.remove(id);
        }
    }
}
