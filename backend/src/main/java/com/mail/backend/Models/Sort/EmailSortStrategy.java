package com.mail.backend.Models.Sort;

import com.mail.backend.Models.Email.Email;
import java.util.ArrayList;

public interface EmailSortStrategy {
    public ArrayList<Email> sort(ArrayList<Email> emails);
}
