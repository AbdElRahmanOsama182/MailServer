package com.mail.backend.Models.Folder;

public class DraftFolder extends Folder {

    public DraftFolder(int id, int userId) {
        super("Drafts", id, userId);
        return;
    }
}
