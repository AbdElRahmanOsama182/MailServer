package com.mail.backend.Models.Contact;

import java.util.ArrayList;

public class Contact {
    private int id;
    private String username;
    private String name;
    private ArrayList<String> emails;

    public Contact() {
    }

    public Contact(int id, String name, ArrayList<String> emails) {
        this.id = id;
        this.name = name;
        this.emails = emails;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEmail(String email) {
        this.emails.add(email);
    }

    public void addEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public void removeEmail(String email) {
        this.emails.remove(email);
    }

    public void updateContact(Contact contactIn) {
        this.setName(contactIn.name);
        this.addEmails(contactIn.emails);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getEmails() {
        return this.emails;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "Contact [id=" + this.id + ", username=" + this.username + ", name=" + this.name + ", emails="
                + this.emails + "]";
    }

    // updateEmails

}