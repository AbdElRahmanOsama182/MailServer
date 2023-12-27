package com.mail.backend.Models.Contact;

import java.util.ArrayList;

public class Contact {
    private int id;
    private String name;
    private ArrayList<String> emails;

    public Contact() {
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNames(String name) {
        this.name = name;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

}
