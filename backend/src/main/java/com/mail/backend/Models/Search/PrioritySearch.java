package com.mail.backend.Models.Search;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class PrioritySearch implements EmailSearchStrategy {
    @Override
    public ArrayList<Email> search(ArrayList<Email> emails, String query) {
        ArrayList<Email> result = new ArrayList<Email>();
        try {
            int priority = Integer.parseInt(query);
            for (Email email : emails) {
                if (email.getPriority() == priority) {
                    result.add(email);
                }
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

}