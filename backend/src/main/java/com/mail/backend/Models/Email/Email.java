package com.mail.backend.Models.Email;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import org.springframework.ws.mime.Attachment;

import com.mail.backend.Managers.EmailManager;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Filter.AndEmailCriteria;
import com.mail.backend.Models.Filter.EmailCriteria;
import com.mail.backend.Models.Filter.EmailPriorityCriteria;
import com.mail.backend.Models.Filter.EmailSubjectCriteria;
import com.mail.backend.Models.Filter.OrEmailCriteria;


public class Email {
    private int id;
    private String fromUserId;
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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
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

    public Map<String, Object> readEmail() {
        Map<String, Object> email = new HashMap<String, Object>();
        email.put("id", this.getId());
        email.put("fromUserId", this.getFromUserId());
        email.put("to", this.getTo());
        email.put("attachments", this.getAttachments());
        email.put("priority", this.getPriority());
        email.put("subject", this.getSubject());
        email.put("body", this.getBody());
        email.put("isDeleted", this.isDeleted());
        email.put("isDraft", this.isDraft());
        email.put("deleteDate", this.getDeleteDate());
        email.put("sendDate", this.getSendDate());
        return email;
    }

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
    //     Date date = Date.from(Instant.now());
    //     Instant instant = Instant.parse("2021-05-04T10:15:30.00Z");
    //     Date date2 = Date.from(instant);
    //     System.out.println(date);
    //     System.out.println(date2);
    //     System.out.println(EmailManager.getInstance().emails.size());
    //     EmailManager.getInstance().loadEmails();
    //     // EmailManager.getInstance().createEmail(Map.of(
    //     // "fromUserId", 1,
    //     // "priority", 1,
    //     // "subject", "subject1",
    //     // "body", "body1",
    //     // "isDeleted", false,
    //     // "isDraft", true,
    //     // "deleteDate", date));
    //     // EmailManager.getInstance().createEmail(Map.of(
    //     // "fromUserId", 2,
    //     // "priority", 2,
    //     // "subject", "subject2",
    //     // "body", "body2",
    //     // "isDeleted", false,
    //     // "isDraft", false));
    //     // EmailManager.getInstance().createEmail(Map.of(
    //     // "fromUserId", 3,
    //     // "priority", 3,
    //     // "subject", "subject3",
    //     // "body", "body3",
    //     // "isDeleted", true,
    //     // "isDraft", false));
    //     // EmailManager.getInstance().saveEmails();
    //     for (Email email : EmailManager.getInstance().getAllEmails()) {
    //         System.out.println(email.readEmail());
    //     }
    //     System.out.println(EmailManager.getInstance().emails.size());
    // }



}
