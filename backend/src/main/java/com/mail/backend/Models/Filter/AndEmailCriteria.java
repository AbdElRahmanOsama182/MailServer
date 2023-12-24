package com.mail.backend.Models.Filter;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class AndEmailCriteria {
    private ArrayList<EmailCriteria> criterias;

    public AndEmailCriteria(ArrayList<EmailCriteria> criterias) {
        this.criterias = criterias;
    }

    public ArrayList<Email> meetCriteria(ArrayList<Email> emails) {
        ArrayList<Email> result = emails;
        for (EmailCriteria criteria : criterias) {
            result = criteria.meetCriteria(result);
        }
        return result;
    }
}
