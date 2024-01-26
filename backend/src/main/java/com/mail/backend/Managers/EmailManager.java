package com.mail.backend.Managers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ws.mime.Attachment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Email.EmailBuilder;
import com.mail.backend.Models.Sort.BodySort;
import com.mail.backend.Models.Sort.DateSort;
import com.mail.backend.Models.Sort.PrioritySort;
import com.mail.backend.Models.Sort.SubjectSort;
import com.mail.backend.Utils.AttachmentUtils;
import com.mail.backend.Managers.ManagerInterface;
import com.mail.backend.Utils.AttachmentUtils;

@RestController
@RequestMapping("/")
public class EmailManager implements ManagerInterface<Email> {
    // private static final String EMAILS_FILE_PATH = "data/emails.json";
    private static final String EMAILS_FILE_PATH = "backend\\src\\main\\data\\emails.json";
    private static EmailManager instance;
    public Map<Integer, Email> emails = new HashMap<Integer, Email>();
    private int nextId = 0;

    private EmailManager() {
    }

    public static synchronized EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
            instance.loadEmails();
        }
        return instance;
    }

    public int getNextId() {
        return this.nextId;
    }

    public Email get(Object id) {
        return this.getEmail((int) id);
    }

    private Email getEmail(int id) {
        System.out.println("All emails: " + this.emails);
        return this.emails.get(id);
    }

    public Email add(Email email) {
        return this.addEmail(email);
    }

    private Email addEmail(Email email) {
        if (email == null) {
            return null;
        }

        System.out.println("Adding email: " + email.readEmail());
        email.setId(this.nextId);
        System.out.println("Adding email with id: " + this.nextId);
        this.emails.put(this.nextId, email);
        this.nextId++;
        saveEmails();
        return email;
    }

    public void remove(Object id) {
        this.removeEmail((int) id);
    }

    private void removeEmail(int id) {
        this.emails.remove(id);
        saveEmails();
    }

    public void updateEmail(int emailId, Map<String, Object> email) {
        this.emails.get(emailId).updateEmail(email);
        saveEmails();
    }

    public Email createEmail(Map<String, Object> email) {
        return addEmail(EmailBuilder.build(email));
    }

    public Map<Object, Email> getAll() {
        return new HashMap<Object, Email>(this.emails);
    }

    public ArrayList<Email> getAllEmails() {
        return new ArrayList<Email>(this.emails.values());
    }

    public void printEmails() {
        for (Email email : this.emails.values()) {
            System.out.println(email.readEmail());
        }
    }

    public void saveEmails() {
        System.out.println("Saving emails");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(this.getAllEmails());
            Path path = Paths.get(EMAILS_FILE_PATH);
            System.out.println(json);
            Files.writeString(path, json);
            System.out.println("Saved emails");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<Email> sort(ArrayList<Email> emails, String sortStrategy) {
        if (sortStrategy.equals("subject")) {
            return new SubjectSort().sort(emails);
        } else if (sortStrategy.equals("body")) {
            return new BodySort().sort(emails);
        } else if (sortStrategy.equals("priority")) {
            return new PrioritySort().sort(emails);
        } else if (sortStrategy.equals("date")) {
            return new DateSort().sort(emails);
        } else {
            return emails;
        }
    }

    public void loadEmails() {
        try {
            // Path path = Paths.get(EMAILS_FILE_PATH);
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Email> emails = mapper.readValue(new File(EMAILS_FILE_PATH),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, Email.class));
            this.nextId = 0;
            for (Email email : emails) {
                this.nextId = Math.max(this.nextId, email.getId() + 1);
                this.emails.put(email.getId(), email);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        printEmails();
    }

}
