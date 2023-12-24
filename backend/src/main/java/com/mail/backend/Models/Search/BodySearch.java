package com.mail.backend.Models.Search;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class BodySearch implements EmailSearchStrategy {

    @Override
    public ArrayList<Email> search(ArrayList<Email> emails, String query) {
        ArrayList<Email> result = new ArrayList<Email>();
        for (Email email : emails) {
            if (email.getBody().contains(query)) {
                result.add(email);
            }
        }
        return result;
    }

}
