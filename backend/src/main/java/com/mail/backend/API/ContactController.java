package com.mail.backend.API;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mail.backend.Managers.ContactManager;
import com.mail.backend.Models.Contact.Contact;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class ContactController {

    private ContactManager contactManager = ContactManager.getInstance();
    
    @GetMapping("/contacts")
    public ArrayList<Contact> getAllContacts() {
        return contactManager.getAllContacts();
    }


    @PostMapping("/contacts")
    public Contact createContact(@RequestBody Contact contact) {
        return contactManager.add(contact);
    }

    @DeleteMapping("/contacts/{id}")
    public void deleteContact(@PathVariable int id) {
        contactManager.remove(id);
    }

    @PutMapping("/contacts/{id}")
    public void updateContact(@PathVariable int id, @RequestBody Contact contact) {
        contactManager.updateContact(id, contact);
    }

    @PostMapping("/contacts/{id}/emails")
    public void addEmailToContact(@PathVariable int id, @RequestBody String email) {
        contactManager.addEmailToContact(id, email);
    }

    @DeleteMapping("/contacts/{id}/emails")
    public void removeEmailFromContact(@PathVariable int id, @RequestBody String email) {
        contactManager.removeEmailFromContact(id, email);
    }

    @GetMapping("/contacts/sort")
    public ArrayList<Contact> sortContacts() {
        return contactManager.sortContacts(contactManager.getAllContacts());
    }

    @GetMapping("/contacts/search/{name}")
    public ArrayList<Contact> searchContacts(@PathVariable String name) {
        return contactManager.searchContacts(name);
    }
    
}
