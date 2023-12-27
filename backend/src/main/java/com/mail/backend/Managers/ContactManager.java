package com.mail.backend.Managers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.backend.Models.Contact.Contact;

@Component
public class ContactManager {
    private static final String CONTACTS_FILE_PATH = "backend\\src\\main\\java\\com\\mail\\backend\\data\\contacts.json";
    private static ContactManager instance;
    public Map<Integer, Contact> contacts = new HashMap<Integer, Contact>();
    private int nextId = 0;

    private ContactManager() {
    }

    public static synchronized ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
            instance.loadContacts();
        }
        return instance;
    }

    public int getNextId() {
        return this.nextId;
    }

    public Contact getContact(int id) {
        return this.contacts.get(id);
    }

    public void addContact(Contact contact) {
        printContacts();
        System.out.println(this.nextId);
        if (contact == null) {
            return;
        }
        contact.setId(this.nextId);
        this.contacts.put(this.nextId, contact);
        this.nextId++;
        saveContacts();
    }

    public void removeContact(int id) {
        this.contacts.remove(id);
        saveContacts();
    }

    public Contact updateContact(Contact contact) {
        this.contacts.get(contact.getId()).updateContact(contact);
        saveContacts();
        return this.contacts.get(contact.getId());
    }

    public void addEmailToContact(int contactId, String email) {
        this.contacts.get(contactId).addEmail(email);
        saveContacts();
    }

    public void removeEmailFromContact(int contactId, String email) {
        this.contacts.get(contactId).removeEmail(email);
        saveContacts();
    }

    public ArrayList<Contact> getAllContacts() {
        printContacts();
        return new ArrayList<Contact>(this.contacts.values());
    }

    public ArrayList<Contact> getUserContacts(String username) {
        ArrayList<Contact> userContacts = new ArrayList<Contact>();
        for (Contact contact : this.contacts.values()) {
            if (contact.getUsername().equals(username)) {
                userContacts.add(contact);
            }
        }
        return userContacts;
    }

    public Contact jsonToContact(JsonNode jsonNode) {
        Contact contact = new Contact();
        if (jsonNode.get("id") != null)
            contact.setId(jsonNode.get("id").asInt());
        if (jsonNode.get("name") != null)
            contact.setName(jsonNode.get("name").asText());
        JsonNode emails = jsonNode.get("emails");
        ArrayList<String> emailList = new ArrayList<String>();
        if (emails.isArray()) {
            for (JsonNode emailNode : emails) {
                emailList.add(emailNode.asText());
            }
        }
        contact.addEmails(emailList);
        return contact;
    }

    public List<Contact> sortContacts(String username) {
        List<Contact> sortedContacts = new ArrayList<Contact>(this.getUserContacts(username));
        sortedContacts.sort(Comparator.comparing(Contact::getName));
        return sortedContacts;
    }

    public void loadContacts() {
        try {
            // Path path = Paths.get(CONTACTS_FILE_PATH);
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Contact> contacts = mapper.readValue(new File(CONTACTS_FILE_PATH),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, Contact.class));
            this.nextId = 0;
            for (Contact contact : contacts) {
                this.nextId = Math.max(this.nextId, contact.getId() + 1);
                this.contacts.put(contact.getId(), contact);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        printContacts();
    }

    public void printContacts() {
        for (Contact contact : this.contacts.values()) {
            System.out.println("ID: " + contact.getId());
            System.out.println("Name: " + contact.getName());
            System.out.println("Emails: " + contact.getEmails());
            System.out.println("Username: " + contact.getUsername());
            System.out.println("-------------------------");
        }
    }

    public ArrayList<Contact> searchContact(String name, String username) {
        ArrayList<Contact> result = new ArrayList<>();
        for (Contact contact : this.getUserContacts(username)) {
            if (contact.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(contact);
            }
        }
        return result;
    }

    public void saveContacts() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(this.contacts.values());
            Path path = Paths.get(CONTACTS_FILE_PATH);
            System.out.println(json);
            Files.writeString(path, json);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
