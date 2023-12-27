package com.mail.backend.API;

import java.util.ArrayList;
import java.util.List;

import com.mail.backend.Managers.ContactManager;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
@RequestMapping("/contacts")
public class ContactController {
    @GetMapping
    public ArrayList<Contact> getAllContacts(@RequestHeader String authorization) {
        User user = Auth.getUser(authorization);
        return ContactManager.getInstance().getUserContacts(user.getUsername());
    }

    @GetMapping("/sort")
    public List<Contact> getSortedContacts(@RequestHeader String authorization) {
        User user = Auth.getUser(authorization);
        return ContactManager.getInstance().sortContacts(user.getUsername());
    }

    @PostMapping
    public ResponseEntity<Void> addContact(@RequestHeader String authorization, @RequestBody Contact contact) {
        User user = Auth.getUser(authorization);
        contact.setUsername(user.getUsername());
        System.out.println(contact);
        System.out.println(contact.getName());
        System.out.println(contact.getEmails());
        ContactManager.getInstance().addContact(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeContact(@PathVariable int id) {
        ContactManager.getInstance().removeContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> editContact(@PathVariable int id, @RequestBody Contact updatedContact) {
        Contact contact = ContactManager.getInstance().updateContact(updatedContact);
        if (contact == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    public List<Contact> searchContact(@RequestHeader String authorization, @PathVariable String name) {
        User user = Auth.getUser(authorization);
        return ContactManager.getInstance().searchContact(name, user.getUsername());
    }
}