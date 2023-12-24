package com.mail.backend.Managers;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mail.backend.Models.Folder.DraftFolder;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.Folder.InboxFolder;
import com.mail.backend.Models.Folder.SentFolder;
import com.mail.backend.Models.Folder.TrashFolder;

public class FolderManager {

    private static final String FOLDERS_FLIE_PATH = "src/main/java/com/mail/backend/data/folders.json";
    private static FolderManager instance = null;
    Map<Integer, Folder> folders = new HashMap<Integer, Folder>();
    private int nextId = 0;

    public FolderManager() {
    }

    public static synchronized FolderManager getInstance() {
        if (instance == null) {
            instance = new FolderManager();
        }
        return instance;
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

    public void createDefaultFolders(int userId) {
        this.folders.put(this.nextId, new InboxFolder(this.nextId, userId));
        this.nextId++;
        this.folders.put(this.nextId, new DraftFolder(this.nextId, userId));
        this.nextId++;
        this.folders.put(this.nextId, new SentFolder(this.nextId, userId));
        this.nextId++;
        this.folders.put(this.nextId, new TrashFolder(this.nextId, userId));
        this.nextId++;
    }

    public void createFolder(String name, int userId) {
        this.folders.put(this.nextId, new Folder(name, this.nextId, userId));
        this.nextId++;
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

    public void deleteEmail(int emailId, int folderId) {
        this.folders.get(folderId).removeEmail(emailId);
        for (Folder folder : this.folders.values()) {
            if (folder.getName().equals("Trash") && folder.getUserId() == this.folders.get(folderId).getUserId()) {
                folder.addEmail(emailId);
                return;
            }
        }
    }

    public void restoreEmail(int emailId, int userId) {
        for (Folder folder : this.folders.values()) {
            if (folder.getName().equals("Inbox") && folder.getUserId() == userId
                    && EmailManager.getInstance().getEmail(emailId).isDraft() == false) {
                folder.addEmail(emailId);
                return;
            }
            if (folder.getName().equals("Draft") && folder.getUserId() == userId
                    && EmailManager.getInstance().getEmail(emailId).isDraft() == true) {
                folder.addEmail(emailId);
                return;
            }
            if (folder.getName().equals("Trash") && folder.getUserId() == userId) {
                folder.removeEmail(emailId);
                return;
            }
        }
    }

    public ArrayList<Folder> getUserFolders(int userId) {
        ArrayList<Folder> userFolders = new ArrayList<Folder>();
        for (Folder folder : this.folders.values()) {
            if (folder.getUserId() == userId) {
                userFolders.add(folder);
            }
        }
        return userFolders;
    }

    public ArrayList<Folder> getAllFolders() {
        return new ArrayList<Folder>(this.folders.values());
    }

    public void setFolders(Map<Integer, Folder> folders) {
        this.folders = folders;
    }

    public void saveFolders() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(getAllFolders());
            Path path = Paths.get(FOLDERS_FLIE_PATH);
            Files.writeString(path, json);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadFolders() {
        try {
            Path path = Paths.get(FOLDERS_FLIE_PATH);
            String json = Files.readString(path);
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Folder> folders = mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, Folder.class));
            this.folders = new HashMap<Integer, Folder>();
            for (Folder folder : folders) {
                this.folders.put(folder.getId(), folder);
            }
            this.nextId = this.folders.size();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
