package com.mail.backend.Models.Email;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.ws.mime.Attachment;


import com.mail.backend.Managers.EmailManager;

import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Afile.*;

public class EmailBuilder {
    private Email email;

    public EmailBuilder() {
        this.email = new Email();
    }

    public EmailBuilder id(int id) {
        this.email.setId(id);
        return this;
    }

    public EmailBuilder fromUserId(String fromUserId) {
        this.email.setFromUserId(fromUserId);
        return this;
    }

    public EmailBuilder to(ArrayList<Contact> to) {
        this.email.setTo(to);
        return this;
    }

    public EmailBuilder attachments(ArrayList<Afile> attachments) {
        this.email.setAttachments(attachments);
        return this;
    }

    public EmailBuilder priority(int priority) {
        this.email.setPriority(priority);
        return this;
    }

    public EmailBuilder subject(String subject) {
        this.email.setSubject(subject);
        return this;
    }

    public EmailBuilder body(String body) {
        this.email.setBody(body);
        return this;
    }

    public EmailBuilder isDeleted(boolean isDeleted) {
        this.email.setDeleted(isDeleted);
        return this;
    }

    public EmailBuilder isDraft(boolean isDraft) {
        this.email.setDraft(isDraft);
        return this;
    }

    public EmailBuilder deleteDate(Date deleteDate) {
        this.email.setDeleteDate(deleteDate);
        return this;
    }

    public EmailBuilder sendDate(Date sendDate) {
        this.email.setSendDate(sendDate);
        return this;
    }

    public Email build() {
        return this.email;
    }

    public static Email build(Map<String, Object> email) {
        EmailBuilder builder = new EmailBuilder();
        for (String key : email.keySet()) {
            switch (key) {
                case "id":
                    builder.id((int) email.get(key));
                    break;
                case "fromUserId":
                    builder.fromUserId((String) email.get(key));
                    break;
                case "to":
                    builder.to((ArrayList<Contact>) email.get(key));
                    break;
                case "attachments":
                    builder.attachments((ArrayList<Afile>) email.get(key));
                    break;
                case "priority":
                    builder.priority((int) email.get(key));
                    break;
                case "subject":
                    builder.subject((String) email.get(key));
                    break;
                case "body":
                    builder.body((String) email.get(key));
                    break;
                case "isDeleted":
                    builder.isDeleted((boolean) email.get(key));
                    break;
                case "isDraft":
                    builder.isDraft((boolean) email.get(key));
                    break;
                case "deleteDate":
                    builder.deleteDate((Date) email.get(key));
                    break;
                case "sendDate":
                    builder.sendDate((Date) email.get(key));
                    break;
                default:
                    break;
            }
        }
        return builder.build();
    }
}