package com.mail.backend.Managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ws.mime.Attachment;

import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Email.EmailBuilder;

public class EmailManager {
    private static EmailManager instance;
    private Map<Integer, Email> emails = new HashMap<Integer, Email>();
    private int nextId = 0;

    private EmailManager() {
    }

    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance;
    }

    public Email getEmail(int id) {
        return this.emails.get(id);
    }

    public void addEmail(Email email) {
        if (email == null) {
            return;
        }
        email.setId(this.nextId);
        this.emails.put(this.nextId, email);
        this.nextId++;
    }

    public void removeEmail(int id) {
        this.emails.remove(id);
    }

    public void updateEmail(int emailId, Map<String, Object> email) {
        this.emails.get(emailId).updateEmail(email);
    }

    public void createEmail(Map<String, Object> email) {
        email.put("id", this.nextId);
        this.emails.put(this.nextId, EmailBuilder.build(email));
    }

}
