package com.mail.backend.Models.Sort;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class ReciversSort implements EmailSortStrategy {

    @Override
    public ArrayList<Email> sort(ArrayList<Email> emails) {
        ArrayList<Email> sortedEmails = emails;
        // Sort by to Size
        for (int i = 0; i < sortedEmails.size(); i++) {
            for (int j = i + 1; j < sortedEmails.size(); j++) {
                if (sortedEmails.get(i).getTo().size() < sortedEmails.get(j).getTo().size()) {
                    Email temp = sortedEmails.get(i);
                    sortedEmails.set(i, sortedEmails.get(j));
                    sortedEmails.set(j, temp);
                }
            }
        }
        return sortedEmails;
    }

}
