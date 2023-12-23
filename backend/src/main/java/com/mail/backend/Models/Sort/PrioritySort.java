package com.mail.backend.Models.Sort;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class PrioritySort implements EmailSortStrategy {
    @Override
    public ArrayList<Email> sort(ArrayList<Email> emails) {
        ArrayList<Email> sortedEmails = new ArrayList<Email>();
        // Sort by 4 priority levels: 4, 3, 2, 1
        for (int i = 4; i > 0; i--) {
            for (Email email : emails) {
                if (email.getPriority() == i) {
                    sortedEmails.add(email);
                }
            }
        }
        return sortedEmails;
    }

}
