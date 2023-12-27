package com.mail.backend.Managers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Sort.ContactSort;

public class ContactManager implements ManagerInterface<Contact> {
    private static final String CONTACTS_FILE_PATH = "src/main/java/com/mail/backend/data/contacts.json";
    private static ContactManager instance;
    public Map<Integer, Contact> contacts = new HashMap<Integer, Contact>();

    private ContactManager() {
    }

    public static synchronized ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
            instance.loadContacts();
        }
        return instance;
    }

    public Contact get(Object id) {
        return this.getContact((int) id);
    }

    private Contact getContact(int id) {
        System.out.println("All contacts: " + this.contacts);
        return this.contacts.get(id);
    }

    public Map<Object, Contact> getAll() {
        return new HashMap<Object, Contact>(this.contacts);
    }

    public ArrayList<Contact> getAllContacts() {
        return new ArrayList<Contact>(this.contacts.values());
    }

    public Contact add(Contact t) {
        return this.addContact(t);
    }

    private Contact addContact(Contact contact) {
        if (contact == null) {
            return null;
        }
        contact.setId(this.contacts.size());
        //check if the contact id is already in the contacts map
        int newId = contact.getId();
        while (this.contacts.containsKey(contact.getId())) {
            contact.setId(contact.getId() + 1);
            newId = contact.getId();
        }

        this.contacts.put(newId, contact);
        saveContacts();

        return contact;
    }

    public void remove(Object id) {
        this.removeContact((int) id);
    }

    private void removeContact(int id) {
        this.contacts.remove(id);
        saveContacts();
    }
    
    //update the data in a contact by knowing its id and getting a new contact
    public void updateContact(int contactId, Contact contact) {
        this.contacts.get(contactId).setNames(contact.getName());
        this.contacts.get(contactId).setEmails(contact.getEmails());
        saveContacts();
    }

    //remove an email from a contact
    public void removeEmailFromContact(int contactId, String email) {
        this.contacts.get(contactId).getEmails().remove(email);
        saveContacts();
    }

    //add an email to a contact
    public void addEmailToContact(int contactId, String email) {
        this.contacts.get(contactId).getEmails().add(email);
        saveContacts();
    }

    //sort contacts by name
    public ArrayList<Contact> sortContacts(ArrayList<Contact> contacts) {
        return new ContactSort().sort(contacts);
    }

    //search a contact by name
    public ArrayList<Contact> searchContacts(String name) {
        ArrayList<Contact> contacts = this.getAllContacts();
        ArrayList<Contact> searchedContacts = new ArrayList<Contact>();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(name.toLowerCase())) {
                searchedContacts.add(contact);
            }
        }
        return searchedContacts;
    }

    public void saveContacts() {
        System.out.println("Saving contacts");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(this.getAllContacts());
            Path path = Paths.get(CONTACTS_FILE_PATH);
            System.out.println(json);
            Files.writeString(path, json);
            System.out.println("Saved contacts");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadContacts() {
        System.out.println("Loading contacts");
        try {
            Path path = Paths.get(CONTACTS_FILE_PATH);
            String json = Files.readString(path);
            ObjectMapper mapper = new ObjectMapper();
            Contact[] contacts = mapper.readValue(json, Contact[].class);
            for (Contact contact : contacts) {
                this.addContact(contact);
            }
            System.out.println("Loaded contacts");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    
}
