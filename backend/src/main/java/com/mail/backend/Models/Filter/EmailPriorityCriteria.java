package com.mail.backend.Models.Filter;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class EmailPriorityCriteria implements EmailCriteria {
    private int priority;

    public EmailPriorityCriteria(int priority) {
        this.priority = priority;
    }

    public ArrayList<Email> meetCriteria(ArrayList<Email> emails) {
        ArrayList<Email> result = new ArrayList<Email>();
        for (Email email : emails) {
            if (email.getPriority() == priority) {
                result.add(email);
            }
        }
        return result;
    }

}