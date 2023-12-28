package com.mail.backend.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mail.backend.Managers.ContactManager;
import com.mail.backend.Managers.ManagerFactory;
import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mail.backend.Managers.ContactManager;
import com.mail.backend.Managers.ManagerFactory;
import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.Contact.Contact;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class ContactController {
    int itemsPage = 5;
    private ContactManager contactManager = (ContactManager) ManagerFactory.getManager("ContactManager");

    @GetMapping("/contacts")
    public Map<String, Object> getAllContacts(@RequestHeader String authorization,
            @RequestParam(required = false) Integer page) {
        User user = Auth.getUser(authorization);
        ArrayList<Contact> contacts = contactManager.getUserContacts(user.getUsername());
        int pages = (int) Math.ceil((double) contacts.size() / itemsPage);
        System.out.println(contacts);
        if (page != null && (int) Math.ceil((double) contacts.size() / itemsPage) >= page) {
            List<Contact> pageList = contacts.subList((page - 1) * itemsPage,
                    Math.min(itemsPage * page, contacts.size()));
            contacts = new ArrayList<Contact>();
            for (Contact contact : pageList)
                contacts.add(contact);

        }
        return Map.of("contacts", contacts, "total", contacts.size(), "pages", pages);
    }

    @PostMapping("/contacts")
    public String createContact(@RequestHeader String authorization, @RequestBody Contact contact) {
        User user = Auth.getUser(authorization);
        contact.setUsername(user.getUsername());
        System.out.println(contact);
        System.out.println(contact.getName());
        System.out.println(contact.getEmails());
        UserManager userManager = (UserManager) ManagerFactory.getManager("UserManager");
        for (String email : contact.getEmails()) {
            if (userManager.getUserByEmail(email) == null) {
                return "Email " + email + " does not exist";
            }
        }
        ContactManager.getInstance().addContact(contact);
        return "Contact added successfully";
    }

    @DeleteMapping("/contacts/{id}")
    public void removeContact(@PathVariable int id) {
        contactManager.removeContact(id);
    }

    @PutMapping("/contacts/{id}")
    public String updateContact(@PathVariable int id, @RequestBody Contact updatedContact) {
        UserManager userManager = (UserManager) ManagerFactory.getManager("UserManager");
        for (String email : updatedContact.getEmails()) {
            if (userManager.getUserByEmail(email) == null) {
                return "Email " + email + " does not exist";
            }
        }
        Contact contact = contactManager.updateContact(updatedContact);
        if (contact == null) {
            return "Contact not found";
        }
        return "Contact updated successfully";
    }

    @GetMapping("/contacts/sort")
    public List<Contact> sortedContacts(@RequestHeader String authorization) {
        User user = Auth.getUser(authorization);
        return contactManager.sortContacts(user.getUsername());
    }

    @GetMapping("contacts/search/{name}")
    public List<Contact> searchContact(@RequestHeader String authorization, @PathVariable String name) {
        User user = Auth.getUser(authorization);
        return contactManager.searchContact(name, user.getUsername());
    }
}