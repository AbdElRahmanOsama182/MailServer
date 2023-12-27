package com.mail.backend.Models.Sort;

import java.util.ArrayList;

import com.mail.backend.Models.Contact.Contact;

public class ContactSort {

    public ArrayList<Contact> sort(ArrayList<Contact> contacts) {
        ArrayList<Contact> sortedContacts = contacts;
        // Sort by name alphabetically
        for (int i = 0; i < sortedContacts.size(); i++) {
            for (int j = i + 1; j < sortedContacts.size(); j++) {
                if (sortedContacts.get(i).getName().compareTo(sortedContacts.get(j).getName()) > 0) {
                    Contact temp = sortedContacts.get(i);
                    sortedContacts.set(i, sortedContacts.get(j));
                    sortedContacts.set(j, temp);
                }
            }
        }
        return sortedContacts;
    }

}