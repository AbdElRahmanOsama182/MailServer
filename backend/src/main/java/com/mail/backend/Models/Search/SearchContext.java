package com.mail.backend.Models.Search;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public class SearchContext {
    public static ArrayList<Email> search(String strategy, ArrayList<Email> emails, String query) {
        switch (strategy) {
            case "subject":
                return new SubjectSearch().search(emails, query);
            case "body":
                return new BodySearch().search(emails, query);
            case "date":
                return new DateSearch().search(emails, query);
            case "priority":
                return new PrioritySearch().search(emails, query);
            case "sender":
                return new SenderSearch().search(emails, query);
            default:
                return emails;
        }
    }
}
