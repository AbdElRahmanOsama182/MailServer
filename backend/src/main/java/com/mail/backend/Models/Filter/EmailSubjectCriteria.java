package com.mail.backend.Models.Filter;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class EmailSubjectCriteria implements EmailCriteria {
    private String subject;

    public EmailSubjectCriteria(String subject) {
        this.subject = subject;
    }

    public ArrayList<Email> meetCriteria(ArrayList<Email> emails) {
        ArrayList<Email> result = new ArrayList<Email>();
        for (Email email : emails) {
            if (email.getSubject().equals(subject)) {
                result.add(email);
            }
        }
        return result;
    }

}
