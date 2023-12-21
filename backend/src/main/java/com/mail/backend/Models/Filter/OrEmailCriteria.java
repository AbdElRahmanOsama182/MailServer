package com.mail.backend.Models.Filter;

import java.util.ArrayList;
import java.util.Set;

import com.mail.backend.Models.Email.Email;

public class OrEmailCriteria implements EmailCriteria {
    private ArrayList<EmailCriteria> criterias;

    public OrEmailCriteria(ArrayList<EmailCriteria> criterias) {
        this.criterias = criterias;
    }

    public ArrayList<Email> meetCriteria(ArrayList<Email> emails) {
        Set<Email> set = new java.util.HashSet<Email>();
        for (EmailCriteria criteria : criterias) {
            set.addAll(criteria.meetCriteria(emails));
        }
        return new ArrayList<Email>(set);
    }

}
