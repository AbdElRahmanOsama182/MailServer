package com.mail.backend.Models.Email;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.ws.mime.Attachment;

import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Filter.AndEmailCriteria;
import com.mail.backend.Models.Filter.EmailCriteria;
import com.mail.backend.Models.Filter.EmailPriorityCriteria;
import com.mail.backend.Models.Filter.EmailSubjectCriteria;
import com.mail.backend.Models.Filter.OrEmailCriteria;

public class Email {
    private int id;
    private int fromUserId;
    private ArrayList<Contact> to;
    private ArrayList<Attachment> attachments;
    private int priority;
    private String subject;
    private String body;
    private boolean isDeleted;
    private boolean isDraft;
    private Date deleteDate;
    private Date sendDate;

    public Email() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public ArrayList<Contact> getTo() {
        return to;
    }

    public void setTo(ArrayList<Contact> to) {
        this.to = to;
    }

    public void addTo(Contact contact) {
        this.to.add(contact);
    }

    public void removeTo(Contact contact) {
        this.to.remove(contact);
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    // public void print() {
    // System.out.println("Email: " + this.id + " " + this.subject + " " +
    // this.priority);
    // }

    public void updateEmail(Map<String, Object> email) {
        for (String key : email.keySet()) {
            switch (key) {
                case "to":
                    this.setTo((ArrayList<Contact>) email.get(key));
                    break;
                case "attachments":
                    this.setAttachments((ArrayList<Attachment>) email.get(key));
                    break;
                case "priority":
                    this.setPriority((int) email.get(key));
                    break;
                case "subject":
                    this.setSubject((String) email.get(key));
                    break;
                case "body":
                    this.setBody((String) email.get(key));
                    break;
                case "isDeleted":
                    this.setDeleted((boolean) email.get(key));
                    break;
                case "isDraft":
                    this.setDraft((boolean) email.get(key));
                    break;
                case "deleteDate":
                    this.setDeleteDate((Date) email.get(key));
                    break;
                case "sendDate":
                    this.setSendDate((Date) email.get(key));
                    break;
                default:
                    break;
            }
        }
    }

    // public static void main(String[] args) {
    // ArrayList<Email> emails = new ArrayList<Email>();
    // // test criteria
    // emails.add(new EmailBuilder().build(Map.of("id", 1, "subject", "subject1",
    // "priority", 1)));
    // emails.add(new EmailBuilder().build(Map.of("id", 2, "subject", "subject2",
    // "priority", 2)));
    // emails.add(new EmailBuilder().build(Map.of("id", 3, "subject", "subject1",
    // "priority", 2)));
    // emails.add(new EmailBuilder().build(Map.of("id", 4, "subject", "subject2",
    // "priority", 1)));
    // emails.add(new EmailBuilder().build(Map.of("id", 5, "subject", "subject1",
    // "priority", 1)));
    // Email testUpdate = new EmailBuilder().build(Map.of("id", 6, "subject",
    // "subject1", "priority", 1));
    // testUpdate.print();
    // testUpdate.updateEmail(Map.of("subject", "subject2", "priority", 2));
    // testUpdate.print();
    // EmailCriteria criteria1 = new EmailSubjectCriteria("subject1");
    // EmailCriteria criteria2 = new EmailPriorityCriteria(1);
    // ArrayList<Email> result1 = criteria1.meetCriteria(emails);
    // ArrayList<Email> result2 = criteria2.meetCriteria(emails);
    // ArrayList<Email> result3 = new AndEmailCriteria(new
    // ArrayList<EmailCriteria>() {
    // {
    // add(criteria1);
    // add(criteria2);
    // }
    // }).meetCriteria(emails);
    // ArrayList<Email> result4 = new OrEmailCriteria(new ArrayList<EmailCriteria>()
    // {
    // {
    // add(criteria2);
    // add(criteria1);
    // }
    // }).meetCriteria(emails);
    // System.out.println("result1: ");
    // for (Email email : result1) {
    // email.print();
    // }
    // System.out.println("result2: ");
    // for (Email email : result2) {
    // email.print();
    // }
    // System.out.println("result3: ");
    // for (Email email : result3) {
    // email.print();
    // }
    // System.out.println("result4: ");
    // for (Email email : result4) {
    // email.print();
    // }

    // }
}
