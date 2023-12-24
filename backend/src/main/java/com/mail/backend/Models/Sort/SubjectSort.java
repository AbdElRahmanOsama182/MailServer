package com.mail.backend.Models.Sort;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class SubjectSort implements EmailSortStrategy {
    @Override
    public ArrayList<Email> sort(ArrayList<Email> emails) {
        ArrayList<Email> sortedEmails = emails;
        // Sort by Subject alphabetically
        for (int i = 0; i < sortedEmails.size(); i++) {
            for (int j = i + 1; j < sortedEmails.size(); j++) {
                if (sortedEmails.get(i).getSubject().compareTo(sortedEmails.get(j).getSubject()) > 0) {
                    Email temp = sortedEmails.get(i);
                    sortedEmails.set(i, sortedEmails.get(j));
                    sortedEmails.set(j, temp);
                }
            }
        }
        return sortedEmails;
    }

}
