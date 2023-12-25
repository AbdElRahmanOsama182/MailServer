package com.mail.backend.Models.Filter;

import java.util.ArrayList;

import com.mail.backend.Models.Email.Email;

public interface EmailCriteria {
    public ArrayList<Email> meetCriteria(ArrayList<Email> emails);
}
