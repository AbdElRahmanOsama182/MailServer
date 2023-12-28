package com.mail.backend.Models.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.mail.backend.Models.Email.Email;

public class PrioritySort implements EmailSortStrategy {
    @Override
    public ArrayList<Email> sort(ArrayList<Email> emails) {
        ArrayList<Email> sortedEmails = new ArrayList<Email>();
        // Sort by 4 priority levels: 4, 3, 2, 1
        Comparator<Email> comparator = new Comparator<Email>() {
            @Override
            public int compare(Email email1, Email email2) {
                return Integer.valueOf(email2.getPriority()).compareTo(email1.getPriority());
            }
        };
        PriorityQueue<Email> priorityQueue = new PriorityQueue<Email>(comparator);
        priorityQueue.addAll(emails);
        while (!priorityQueue.isEmpty()) {
            sortedEmails.add(priorityQueue.poll());
        }
        return sortedEmails;
    }

}
