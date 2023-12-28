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
import com.mail.backend.Models.Filter.EmailPriorityCriteria;
import com.mail.backend.Models.Filter.EmailSubjectCriteria;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.Search.SearchContext;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

import io.jsonwebtoken.Jwts;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mail.backend.Managers.EmailManager;
import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class FolderController {

    private int itemsPage = 5;

    @GetMapping("/folders/{id}")
    public Folder read(@RequestHeader String authorization, @PathVariable("id") Integer id) {
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        User user = Auth.getUser(authorization);
        Folder folder = folderManager.get(id);
        System.out.println("folder.getUserId()");
        System.out.println(folder.getUserId());
        System.out.println("user.getUsername()");
        System.out.println(user.getUsername());
        if (!folder.getUserId().equals(user.getUsername()))
            return null;

        return folder;
    }

    @GetMapping("/folders")
    public ArrayList<Folder> readAll(@RequestHeader String authorization) {
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        User user = Auth.getUser(authorization);
        ArrayList<Folder> folders = folderManager.getUserFolders(user.getUsername());

        return folders;
    }

    @PostMapping("/folders")
    public Folder create(@RequestHeader String authorization, @RequestBody Folder folder) {
        User user = Auth.getUser(authorization);

        if (user == null || folder.getName() == null)
            return null;
        folder = new Folder(folder.getName(), 0, user.getUsername());

        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        folderManager.add(folder);

        return folder;
    }

    @PutMapping("/folders/{id}")
    public Folder rename(@RequestHeader String authorization, @PathVariable("id") Integer id,
            @RequestBody Folder folder) {
        User user = Auth.getUser(authorization);
        if (user == null || folder.getName() == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder oldFolder = folderManager.get(id);
        if (!oldFolder.getUserId().equals(user.getUsername()))
            return null;
        folderManager.renameFolder(id, folder.getName());

        return folderManager.get(id);
    }

    @DeleteMapping("/folders/{id}")
    public void delete(@RequestHeader String authorization, @PathVariable("id") Integer id) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder folder = folderManager.get(id);
        if (folder == null)
            return;
        if (!folder.getUserId().equals(user.getUsername()))
            return;

        folderManager.remove(id);
    }

    @PutMapping("folders/{id}/emails/{emailId}")
    public Folder moveEmail(@RequestHeader String authorization, @PathVariable("id") Integer id,
            @PathVariable("emailId") Integer emailId) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder folder = folderManager.get(id);
        if (folder == null)
            return null;
        if (!folder.getUserId().equals(user.getUsername()))
            return null;

        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(emailId);
        folderManager.moveEmail(emailId, email.getFolderId(), id);
        return folder;
    }

    @PutMapping("folders/trash/emails/{emailId}")
    public Folder trashEmail(@RequestHeader String authorization, @PathVariable("emailId") Integer emailId) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        System.out.println("Trash email");
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder trashFolder = folderManager.getUserFolderByName(user.getUsername(), "Trash");
        if (trashFolder == null)
            return null;

        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(emailId);
        // mark as deleted
        email.setDeleted(true);
        email.setFolderId(trashFolder.getId());
        emailManager.updateEmail(emailId, Map.of("isDeleted", true));
        folderManager.addEmail(trashFolder.getId(), emailId);
        return trashFolder;
    }

    @PutMapping("folders/trash/emails/{emailId}/restore")
    public Folder restoreEmail(@RequestHeader String authorization, @PathVariable("emailId") Integer emailId) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder trashFolder = folderManager.getUserFolderByName(user.getUsername(), "Trash");
        if (trashFolder == null)
            return null;

        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        Email email = emailManager.get(emailId);
        // mark as deleted
        emailManager.updateEmail(emailId, Map.of("isDeleted", false));
        folderManager.removeEmail(trashFolder.getId(), emailId);
        if (email.isDraft()) {
            Folder draftFolder = folderManager.getUserFolderByName(user.getUsername(), "Draft");
            if (draftFolder == null)
                return null;
            email.setFolderId(draftFolder.getId());
            folderManager.addEmail(draftFolder.getId(), emailId);
        } else if (email.getFromUserId().equals(user.getUsername())) {
            Folder sentFolder = folderManager.getUserFolderByName(user.getUsername(), "Sent");
            if (sentFolder == null)
                return null;
            email.setFolderId(sentFolder.getId());
            folderManager.addEmail(sentFolder.getId(), emailId);
        } else {
            Folder inboxFolder = folderManager.getUserFolderByName(user.getUsername(), "Inbox");
            if (inboxFolder == null)
                return null;
            email.setFolderId(inboxFolder.getId());
            folderManager.addEmail(inboxFolder.getId(), emailId);
        }

        return trashFolder;
    }

    @GetMapping("folders/trash/emails")
    public Map<String,Object> getTrashEmails(@RequestHeader String authorization,

            @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String filterSubject,
            @RequestParam(required = false) Integer filterPriority,
            @RequestParam(required = false) String searchType, @RequestParam(required = false) String searchValue) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder sentFolder = folderManager.getUserFolderByName(user.getUsername(), "Trash");
        if (sentFolder == null)
            return null;
        ArrayList<Email> emails = new ArrayList<Email>();
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        for (int emailId : sentFolder.getEmails()) {
            Email email = emailManager.get(emailId);
                emails.add(email);
        }
        
        if (filterSubject != null) {
            EmailSubjectCriteria emailSubjectCriteria = new EmailSubjectCriteria(filterSubject);
            emails = emailSubjectCriteria.meetCriteria(emails);
        }
        System.out.println("filterPriority: " + filterPriority);

        if (filterPriority != null) {
            EmailPriorityCriteria emailPriorityCriteria = new EmailPriorityCriteria(filterPriority);
            emails = emailPriorityCriteria.meetCriteria(emails);
        }
        if (searchType != null && searchValue != null) {
            System.out.println("Search type: " + searchType);
            System.out.println("Search value: " + searchValue);
            emails = SearchContext.search(searchType, emails, searchValue);
        }
        System.out.println("filterPriority: " + filterPriority);

        if (sort != null)
            emails = emailManager.sort(emails, sort);
        int pages=(int)Math.ceil((double)emails.size()/itemsPage);
        if (page != null && (int)Math.ceil((double)emails.size()/itemsPage) >= page) {
            List<Email> pageList = emails.subList((page - 1) * itemsPage, Math.min(itemsPage* page,emails.size()));

            emails = new ArrayList<Email>();
            for (Email email : pageList)
                emails.add(email);
        }
        System.out.println("Final emails: ");
        for (Email email : emails) {
            System.out.println(email.readEmail());
        }
        return Map.of("emails",emails,"total",emails.size(),"pages",pages);

    }

    @GetMapping("folders/{id}/emails")
    public Map<String,Object> getFolderEmails(@RequestHeader String authorization, @PathVariable("id") Integer id,
            @RequestParam(required = false) String subjectHas, @RequestParam(required = false) String from,
            @RequestParam(required = false) Integer page, @RequestParam(required = false) String sort) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder folder = folderManager.get(id);
        if (folder == null)
            return null;
        if (!folder.getUserId().equals(user.getUsername()))
            return null;
        Comparator<Email> comparator = new Comparator<Email>() {
            @Override
            public int compare(Email email1, Email email2) {
                if (sort == null || !sort.equals("priority"))
                    return email1.getSendDate().compareTo(email2.getSendDate());
                else
                    return Integer.valueOf(email2.getPriority()).compareTo(email1.getPriority());
            }
        };
        PriorityQueue<Email> emailsPQ = new PriorityQueue<Email>(comparator);
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        ArrayList<Email> emails = new ArrayList<Email>();
        for (int emailId : folder.getEmails()) {
            Email email = emailManager.get(emailId);
            if (!email.isDeleted() && (subjectHas == null || email.getSubject().contains(subjectHas))
                    && (from == null || email.getFromUserId().equals(from))) {
                emailsPQ.add(email);
            }
        }
        while (!emailsPQ.isEmpty()) {
            emails.add(emailsPQ.poll());
        }

        int pages=(int)Math.ceil((double)emails.size()/itemsPage);
        if (page != null && (int)Math.ceil((double)emails.size()/itemsPage) >= page) {
            List<Email> pageList = emails.subList((page - 1) * itemsPage, Math.min(itemsPage* page,emails.size()));
            emails = new ArrayList<Email>();
            for (Email email : pageList)
                emails.add(email);

        }
        return Map.of("emails",emails,"total",emails.size(),"pages",pages);
    }

    @GetMapping("folders/draft/emails")
    public Map<String,Object> getDraftEmails(@RequestHeader String authorization,

            @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String filterSubject,
            @RequestParam(required = false) Integer filterPriority,
            @RequestParam(required = false) String searchType, @RequestParam(required = false) String searchValue) {

        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder sentFolder = folderManager.getUserFolderByName(user.getUsername(), "Draft");
        if (sentFolder == null)
            return null;
        ArrayList<Email> emails = new ArrayList<Email>();
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        for (int emailId : sentFolder.getEmails()) {
            Email email = emailManager.get(emailId);
            if (!email.isDeleted()) {
                emails.add(email);
            }
        }

        if (filterSubject != null) {
            EmailSubjectCriteria emailSubjectCriteria = new EmailSubjectCriteria(filterSubject);
            emails = emailSubjectCriteria.meetCriteria(emails);
        }
        System.out.println("filterPriority: " + filterPriority);

        if (filterPriority != null) {
            EmailPriorityCriteria emailPriorityCriteria = new EmailPriorityCriteria(filterPriority);
            emails = emailPriorityCriteria.meetCriteria(emails);
        }
        if (searchType != null && searchValue != null) {
            System.out.println("Search type: " + searchType);
            System.out.println("Search value: " + searchValue);
            emails = SearchContext.search(searchType, emails, searchValue);
        }

        if (sort != null)
            emails = emailManager.sort(emails, sort);
        int pages=(int)Math.ceil((double)emails.size()/itemsPage);
        if (page != null && (int)Math.ceil((double)emails.size()/itemsPage) >= page) {
            List<Email> pageList = emails.subList((page - 1) * itemsPage, Math.min(itemsPage* page,emails.size()));
            emails = new ArrayList<Email>();
            for (Email email : pageList)
                emails.add(email);

        }
        System.out.println("Final emails: ");
        for (Email email : emails) {
            System.out.println(email.readEmail());
        }
        return Map.of("emails",emails,"total",emails.size(),"pages",pages);
    }

    @GetMapping("folders/sent/emails")

    public Map<String,Object> getSentEmails(@RequestHeader String authorization,

            @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String filterSubject,
            @RequestParam(required = false) Integer filterPriority,
            @RequestParam(required = false) String searchType, @RequestParam(required = false) String searchValue) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder sentFolder = folderManager.getUserFolderByName(user.getUsername(), "Sent");
        if (sentFolder == null)
            return null;
        ArrayList<Email> emails = new ArrayList<Email>();
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        for (int emailId : sentFolder.getEmails()) {
            Email email = emailManager.get(emailId);
            if (!email.isDeleted()) {
                emails.add(email);
            }
        }

        if (filterSubject != null) {
            EmailSubjectCriteria emailSubjectCriteria = new EmailSubjectCriteria(filterSubject);
            emails = emailSubjectCriteria.meetCriteria(emails);
        }
        System.out.println("filterPriority: " + filterPriority);

        if (filterPriority != null) {
            EmailPriorityCriteria emailPriorityCriteria = new EmailPriorityCriteria(filterPriority);
            emails = emailPriorityCriteria.meetCriteria(emails);
        }
        if (searchType != null && searchValue != null) {
            System.out.println("Search type: " + searchType);
            System.out.println("Search value: " + searchValue);
            emails = SearchContext.search(searchType, emails, searchValue);
        }

        if (sort != null)
            emails = emailManager.sort(emails, sort);
        int pages=(int)Math.ceil((double)emails.size()/itemsPage);
        if (page != null && (int)Math.ceil((double)emails.size()/itemsPage) >= page) {
            List<Email> pageList = emails.subList((page - 1) * itemsPage, Math.min(itemsPage* page,emails.size()));
            emails = new ArrayList<Email>();
            for (Email email : pageList)
                emails.add(email);
        }
        System.out.println("Final emails: ");
        for (Email email : emails) {
            System.out.println(email.readEmail());
        }
        return Map.of("emails",emails,"total",emails.size(),"pages",pages);
    }

    @GetMapping("folders/inbox/emails")
    public Map<String,Object> getInboxEmails(@RequestHeader String authorization,

            @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String filterSubject,
            @RequestParam(required = false) Integer filterPriority,
            @RequestParam(required = false) String searchType, @RequestParam(required = false) String searchValue) {
        User user = Auth.getUser(authorization);
        if (user == null)
            return null;
        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        Folder sentFolder = folderManager.getUserFolderByName(user.getUsername(), "Inbox");
        if (sentFolder == null)
            return null;
        ArrayList<Email> emails = new ArrayList<Email>();
        EmailManager emailManager = (EmailManager) ManagerFactory.getManager("EmailManager");
        for (int emailId : sentFolder.getEmails()) {
            Email email = emailManager.get(emailId);
            if (!email.isDeleted()) {
                emails.add(email);
            }
        }

        if (filterSubject != null) {
            EmailSubjectCriteria emailSubjectCriteria = new EmailSubjectCriteria(filterSubject);
            emails = emailSubjectCriteria.meetCriteria(emails);
        }
        System.out.println("filterPriority: " + filterPriority);

        if (filterPriority != null) {
            EmailPriorityCriteria emailPriorityCriteria = new EmailPriorityCriteria(filterPriority);
            emails = emailPriorityCriteria.meetCriteria(emails);
        }
        if (searchType != null && searchValue != null) {
            System.out.println("Search type: " + searchType);
            System.out.println("Search value: " + searchValue);
            emails = SearchContext.search(searchType, emails, searchValue);
        }
        
        if (sort != null)
            emails = emailManager.sort(emails, sort);
        int pages=(int)Math.ceil((double)emails.size()/itemsPage);
        if (page != null && (int)Math.ceil((double)emails.size()/itemsPage) >= page) {
            List<Email> pageList = emails.subList((page - 1) * itemsPage, Math.min(itemsPage* page,emails.size()));

            emails = new ArrayList<Email>();
            for (Email email : pageList)
                emails.add(email);
        }
        System.out.println("Final emails: ");
        for (Email email : emails) {
            System.out.println(email.readEmail());
        }
        
        return Map.of("emails",emails,"total",emails.size(),"pages",pages);

    }

}
