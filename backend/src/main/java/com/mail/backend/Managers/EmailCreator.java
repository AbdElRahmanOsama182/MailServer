package com.mail.backend.Managers;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import com.mail.backend.Managers.*;
import com.mail.backend.Models.Afile.Afile;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.User.User;



public class EmailCreator {
    
    public static Email createAndSend(Email email,User user){
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        AttachmentManager attachmentManager =(AttachmentManager) ManagerFactory.getManager("AttachmentManager");

    
        // add fields
        email.setFromUserId(user.getUsername());
        email.setSendDate(new Date());
        
        // attachements
        for(int i=0;i<email.getAttachments().size();i++){
            Afile attachment=email.getAttachments().get(i);
            attachment = attachmentManager.get(attachment.getId());
            email.getAttachments().set(i, attachment);
            System.out.println("attachment.getPaths()");
            System.out.println(attachment.getPaths());
        }
        
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


        // send
        Queue<Contact> contacts = new LinkedList<Contact>();

        for(Contact contact:email.getTo()){
            contacts.add(contact);
        }
        final Email finalEmail = email;
        CompletableFuture.runAsync(() -> {
            sendEmails(finalEmail,contacts);
        });
        
        return email;
    }

    public static void sendEmails(Email email,Queue<Contact> contacts){
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        UserManager userManager = (UserManager) ManagerFactory.getManager("UserManager");
        System.out.println("sendEmails to:"+contacts.size()+" contacts");
        System.out.println(contacts);
        while(!contacts.isEmpty()){
            Contact contact = contacts.poll();
            Email sentEmail = emailManager.createEmail(email.readEmail());

            User toUser = userManager.getUserByEmail(contact.getEmails().get(0));
            // send
            Folder inboxFolder = folderManager.getUserFolderByName(toUser.getUsername(), "Inbox");
            sentEmail.setFolderId(inboxFolder.getId());
            folderManager.addEmail(inboxFolder.getId(),sentEmail.getId() );
        }

    }
}