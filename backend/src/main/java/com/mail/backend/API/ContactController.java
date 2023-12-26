package com.mail.backend.API;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mail.backend.Managers.ContactManager;
import com.mail.backend.Models.Contact.Contact;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactManager contactManager;

    public ContactController(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    @GetMapping
    public ArrayList<Contact> getAllContacts() {
        return contactManager.getAllContacts();
    }

    @GetMapping("/sort")
    public List<Contact> getSortedContacts() {
        return contactManager.sortContacts();
    }

    @PostMapping
    public ResponseEntity<Void> addContact(@RequestBody Contact contact) {
        contactManager.addContact(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeContact(@PathVariable int id) {
        contactManager.removeContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> editContact(@PathVariable int id, @RequestBody Contact updatedContact) {
        Contact contact = contactManager.updateContact(updatedContact);
        if (contact == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    public List<Contact> searchContact(@PathVariable String name) {
        return contactManager.searchContact(name);
    }
}