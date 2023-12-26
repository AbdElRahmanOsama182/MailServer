package com.mail.backend.Managers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mail.backend.Managers.*;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.User.User;



public class EmailCreator {
    
    public static Email createAndSend(Email email,User user){
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");

    
        // add fields
        email.setFromUserId(user.getUsername());
        email.setSendDate(new Date());
        
        // add to outbox folder
        Folder outboxFolder = folderManager.getUserFolderByName(user.getUsername(), "Sent");
        email.setFolderId(outboxFolder.getId());
        
        // create email
        email=emailManager.add(email);
        System.out.println("Email id");
        System.out.println(email.getId());


        folderManager.addEmail(outboxFolder.getId(),email.getId() );
        System.out.println("outboxFolder.getEmails()");
        System.out.println(outboxFolder.getEmails());

        // attachements


        // clone and send

        for(Contact contact : email.getTo()){
            // clone
            Email sentEmail = emailManager.createEmail(email.readEmail());

            // send
            Folder inboxFolder = folderManager.getUserFolderByName(contact.getUserId(), "Inbox");
            sentEmail.setFolderId(inboxFolder.getId());
            folderManager.addEmail(inboxFolder.getId(),sentEmail.getId() );

        }

        return email;
    }
}