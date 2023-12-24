package com.mail.backend.Models.Email;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.ws.mime.Attachment;

import com.mail.backend.Models.Contact.Contact;

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

}
