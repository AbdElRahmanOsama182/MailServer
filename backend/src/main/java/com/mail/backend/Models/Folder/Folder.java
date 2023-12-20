package com.mail.backend.Models.Folder;

import java.util.ArrayList;

public class Folder {
    private String name;
    private int id;
    private ArrayList<Integer> emails;

    public Folder(String name, int id) {
        this.name = name;
        this.id = id;
        this.emails = new ArrayList<Integer>();
        return;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        return;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
        return;
    }

    public ArrayList<Integer> getEmails() {
        return this.emails;
    }

    public int getEmail(int emailId) {
        return this.emails.get(emailId);
    }

    public void setEmails(ArrayList<Integer> emailsId) {
        this.emails = emailsId;
        return;
    }

    public void addEmail(int emailId) {
        if (this.emails.contains(emailId)) {
            return;
        }
        this.emails.add(emailId);
        return;
    }

    public void removeEmail(int emailId) {
        if (!this.emails.contains(emailId)) {
            return;
        }
        this.emails.remove(emails.indexOf(emailId));
        return;
    }
}
