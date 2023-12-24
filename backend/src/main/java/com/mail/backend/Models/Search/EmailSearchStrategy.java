package com.mail.backend.Models.Search;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public interface EmailSearchStrategy {
    public ArrayList<Email> search(ArrayList<Email> emails, String query);
}