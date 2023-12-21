package com.mail.backend.Managers;

import java.util.HashMap;
import java.util.Map;

import com.mail.backend.Models.Folder.DraftFolder;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.Folder.InboxFolder;
import com.mail.backend.Models.Folder.SentFolder;
import com.mail.backend.Models.Folder.TrashFolder;

public class FolderManager {
    Map<Integer, Folder> folders;

    public FolderManager() {
        this.folders = new HashMap<Integer, Folder>();
        this.folders.put(0, new InboxFolder());
        this.folders.put(1, new SentFolder());
        this.folders.put(2, new DraftFolder());
        this.folders.put(3, new TrashFolder());
    }

    public Folder getFolder(int folderId) {
        return this.folders.get(folderId);
    }

    public void addFolder(Folder folder) {
        if (folder == null) {
            return;
        }
        this.folders.put(folder.getId(), folder);
    }

    public void removeFolder(int folderId) {
        this.folders.remove(folderId);
    }

    public void createFolder(String name) {
        int id = this.folders.size();
        this.folders.put(id, new Folder(name, id));
    }

    public void renameFolder(int folderId, String name) {
        this.folders.get(folderId).setName(name);
    }

    public void addEmail(int folderId, int emailId) {
        this.folders.get(folderId).addEmail(emailId);
    }

    public void addEmails(int folderId, int[] emailsId) {
        for (int emailId : emailsId) {
            this.folders.get(folderId).addEmail(emailId);
        }
    }

    public void removeEmail(int folderId, int emailId) {
        this.folders.get(folderId).removeEmail(emailId);
    }

    public void moveEmail(int emailId, int fromId, int toId) {
        this.folders.get(fromId).removeEmail(emailId);
        this.folders.get(toId).addEmail(emailId);
    }

    public void copyEmail(int emailId, int folderId) {
        this.folders.get(folderId).addEmail(emailId);
    }

    public void deleteEmail(int emailId) {
        if (this.folders.get(2).getEmails().contains(emailId)) {
            this.folders.get(2).removeEmail(emailId);
            return;
        }
        for (Map.Entry<Integer, Folder> entry : this.folders.entrySet()) {
            if (entry.getKey() == 3) {
                entry.getValue().addEmail(emailId);
                continue;
            }
            entry.getValue().removeEmail(emailId);
        }
    }

    public void restoreEmail(int emailId) {
        if (this.folders.get(3).getEmails().contains(emailId)) {
            this.folders.get(3).removeEmail(emailId);
            this.folders.get(0).addEmail(emailId);
        }
    }

    public Map<Integer, Folder> getFolders() {
        return this.folders;
    }
}
