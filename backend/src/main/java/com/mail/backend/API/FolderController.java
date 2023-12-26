package com.mail.backend.API;

import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mail.backend.Managers.EmailManager;
import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;


@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class FolderController {

    private int itemsPage=5;

    @GetMapping("/folders/{id}")
    public Folder read(@RequestHeader String authorization,@PathVariable("id") Integer id){
        FolderManager folderManager =(FolderManager)ManagerFactory.getManager("FolderManager");
        User user= Auth.getUser(authorization);
        Folder folder = folderManager.get(id);
        System.out.println("folder.getUserId()");
        System.out.println(folder.getUserId());
        System.out.println("user.getUsername()");
        System.out.println(user.getUsername());
        if(!folder.getUserId().equals(user.getUsername())) return null;

        return folder;
    }

    @GetMapping("/folders")
    public ArrayList<Folder> readAll(@RequestHeader String authorization){
        FolderManager folderManager =(FolderManager)ManagerFactory.getManager("FolderManager");
        User user= Auth.getUser(authorization);
        ArrayList<Folder> folders = folderManager.getUserFolders(user.getUsername());

        return folders;
    }
    

    @PostMapping("/folders")
    public Folder create(@RequestHeader String authorization, @RequestBody Folder folder){
        User user=Auth.getUser(authorization);

        if(user==null|| folder.getName()==null) return null;
        folder= new Folder(folder.getName(), 0, user.getUsername());


        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        folderManager.add(folder);

        return folder;
    }

    @PutMapping("/folders/{id}")
    public Folder rename(@RequestHeader String authorization,@PathVariable("id") Integer id, @RequestBody Folder folder){
        User user=Auth.getUser(authorization);
        if(user==null|| folder.getName()==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder oldFolder = folderManager.get(id);
        if(!oldFolder.getUserId().equals(user.getUsername())) return null;
        folderManager.renameFolder(id, folder.getName());
        
        return folderManager.get(id);
    }


    @DeleteMapping("/folders/{id}")
    public void delete(@RequestHeader String authorization,@PathVariable("id") Integer id){
        User user=Auth.getUser(authorization);
        if(user==null) return;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder folder = folderManager.get(id);
        if(folder==null) return;
        if(!folder.getUserId().equals(user.getUsername())) return;
        folderManager.remove(id);
    }

    @PutMapping("folders/{id}/emails/{emailId}")
    public Folder moveEmail(@RequestHeader String authorization,@PathVariable("id") Integer id,@PathVariable("emailId") Integer emailId){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder folder = folderManager.get(id);
        if(folder==null) return null;
        if(!folder.getUserId().equals(user.getUsername())) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(emailId);
        folderManager.moveEmail(emailId, email.getFolderId(), id);
        return folder;
    }

    @PutMapping("folders/trash/emails/{emailId}")
    public Folder trashEmail(@RequestHeader String authorization,@PathVariable("emailId") Integer emailId){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder trashFolder = folderManager.getUserFolderByName(user.getUsername(), "Trash");
        if(trashFolder==null) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(emailId);
        // mark as deleted
        emailManager.updateEmail(emailId, Map.of("isDeleted", true));

        return trashFolder;
    }

    @PutMapping("folders/trash/emails/{emailId}/restore")
    public Folder restoreEmail(@RequestHeader String authorization,@PathVariable("emailId") Integer emailId){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder trashFolder = folderManager.getUserFolderByName(user.getUsername(), "Trash");
        if(trashFolder==null) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(emailId);
        // mark as deleted
        emailManager.updateEmail(emailId, Map.of("isDeleted", false));

        return trashFolder;
    }

    @GetMapping("folders/trash/emails")
    public ArrayList<Email> getTrashEmails(@RequestHeader String authorization, @RequestParam(required = false) String subjectHas, @RequestParam(required = false) String from, @RequestParam(required = false) Integer page){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder trashFolder = folderManager.getUserFolderByName(user.getUsername(), "Trash");
        if(trashFolder==null) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        ArrayList<Email> emails = new ArrayList<Email>();
        for(int emailId: trashFolder.getEmails()){
            Email email = emailManager.get(emailId);
            if(email.isDeleted() && (subjectHas==null || email.getSubject().contains(subjectHas)) && (from==null||email.getFromUserId().equals(from))){
                emails.add(email);
            }
        }

    
        if(page!=null && page*itemsPage<=emails.size()){
            List<Email> pageList= emails.subList((page-1)*itemsPage,(page)*itemsPage);
            emails= new ArrayList<Email>();
            for(Email email : pageList) emails.add(email);
        }

        return emails;
    }

    @GetMapping("folders/{id}/emails")
    public ArrayList<Email> getFolderEmails(@RequestHeader String authorization,@PathVariable("id") Integer id, @RequestParam(required = false) String subjectHas, @RequestParam(required = false) String from, @RequestParam(required = false) Integer page){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder folder = folderManager.get(id);
        if(folder==null) return null;
        if(!folder.getUserId().equals(user.getUsername())) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        ArrayList<Email> emails = new ArrayList<Email>();
        for(int emailId: folder.getEmails()){
            Email email = emailManager.get(emailId);
            if(!email.isDeleted() && (subjectHas==null || email.getSubject().contains(subjectHas)) && (from==null||email.getFromUserId().equals(from))){
                emails.add(email);
            }
        }
        
        if(page!=null && page*itemsPage<=emails.size()){
            List<Email> pageList= emails.subList((page-1)*itemsPage,(page)*itemsPage);
            emails= new ArrayList<Email>();
            for(Email email : pageList) emails.add(email);
        }
        return emails;
    }

    @GetMapping("folders/draft/emails")
    public ArrayList<Email> getDraftEmails(@RequestHeader String authorization, @RequestParam(required = false) Integer page){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        ArrayList<Email> emails = new ArrayList<Email>();
        for(Email email: emailManager.getAllEmails()){
            if(email.isDraft() && email.getFromUserId().equals(user.getUsername()) && !email.isDeleted()){
                emails.add(email);
            }
        }
        
        if(page!=null && page*itemsPage<=emails.size()){
            List<Email> pageList= emails.subList((page-1)*itemsPage,(page)*itemsPage);
            emails= new ArrayList<Email>();
            for(Email email : pageList) emails.add(email);
        }
        return emails;
    }

    @GetMapping("folders/sent/emails")
    public ArrayList<Email> getSentEmails(@RequestHeader String authorization, @RequestParam(required = false) String subjectHas, @RequestParam(required = false) String from, @RequestParam(required = false) Integer page){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder sentFolder = folderManager.getUserFolderByName(user.getUsername(), "Sent");
        if(sentFolder==null) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        ArrayList<Email> emails = new ArrayList<Email>();
        for(int emailId: sentFolder.getEmails()){
            Email email = emailManager.get(emailId);
            if(!email.isDeleted() && (subjectHas==null || email.getSubject().contains(subjectHas)) && (from==null||email.getFromUserId().equals(from))){
                emails.add(email);
            }
        }
        
        if(page!=null && page*itemsPage<=emails.size()){
            List<Email> pageList= emails.subList((page-1)*itemsPage,(page)*itemsPage);
            emails= new ArrayList<Email>();
            for(Email email : pageList) emails.add(email);
        }
        return emails;
    }

    @GetMapping("folders/inbox/emails")
    public ArrayList<Email> getInboxEmails(@RequestHeader String authorization, @RequestParam(required = false) String subjectHas, @RequestParam(required = false) String from, @RequestParam(required = false) Integer page){
        User user=Auth.getUser(authorization);
        if(user==null) return null;
        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        Folder inboxFolder = folderManager.getUserFolderByName(user.getUsername(), "Inbox");
        if(inboxFolder==null) return null;
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        ArrayList<Email> emails = new ArrayList<Email>();
        for(int emailId: inboxFolder.getEmails()){
            Email email = emailManager.get(emailId);
            if(!email.isDeleted() && (subjectHas==null || email.getSubject().contains(subjectHas)) && (from==null||email.getFromUserId().equals(from))){
                emails.add(email);
            }
        }
        if(page!=null && page*itemsPage<=emails.size()){
            List<Email> pageList= emails.subList((page-1)*itemsPage,(page)*itemsPage);
            emails= new ArrayList<Email>();
            for(Email email : pageList) emails.add(email);
        }
        return emails;
    }
    
}
