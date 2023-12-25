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
import com.mail.backend.Managers.ManagerInterface;

public class FolderManager implements ManagerInterface<Folder>{

    private static final String FOLDERS_FILE_PATH = "src/main/java/com/mail/backend/data/folders.json";
    private static FolderManager instance = null;
    Map<Integer, Folder> folders = new HashMap<Integer, Folder>();
    private int nextId = 0;

    public FolderManager() {
    }

    public static synchronized FolderManager getInstance() {
        if (instance == null) {
            instance = new FolderManager();
            instance.loadFolders();
        }
        return instance;

    }

    public Folder get(Object id) {
        return this.getFolder((int) id);
    }

    private Folder getFolder(int folderId) {
        return this.folders.get(folderId);
    }

    public Folder add(Folder folder) {
        return this.addFolder(folder);
    }
    private Folder addFolder(Folder folder) {
        if (folder == null) {
            return null;
        }
        if(folder.getId() == 0){
            folder.setId(this.nextId);
            this.nextId++;
        }
        this.folders.put(folder.getId(), folder);
        this.saveFolders();
        return folder;
    }

    public void remove(Object id) {
        this.removeFolder((int) id);
    }

    private void removeFolder(int folderId) {
        this.folders.remove(folderId);
        this.saveFolders();
    }

    public void createDefaultFolders(String userId) {
        this.folders.put(this.nextId, new InboxFolder(this.nextId, userId));
        this.nextId++;
        this.folders.put(this.nextId, new DraftFolder(this.nextId, userId));
        this.nextId++;
        this.folders.put(this.nextId, new SentFolder(this.nextId, userId));
        this.nextId++;
        this.folders.put(this.nextId, new TrashFolder(this.nextId, userId));
        this.nextId++;
        this.saveFolders();
    }

    public void createFolder(String name, String userId) {
        this.folders.put(this.nextId, new Folder(name, this.nextId, userId));
        this.nextId++;
        this.saveFolders();
    }

    public void renameFolder(int folderId, String name) {
        this.folders.get(folderId).setName(name);
        this.saveFolders();
    }

    public void addEmail(int folderId, int emailId) {
        this.folders.get(folderId).addEmail(emailId);
        this.saveFolders();
    }

    public void addEmails(int folderId, int[] emailsId) {
        for (int emailId : emailsId) {
            this.folders.get(folderId).addEmail(emailId);
        }
        this.saveFolders();
    }

    public void removeEmail(int folderId, int emailId) {
        this.folders.get(folderId).removeEmail(emailId);
        this.saveFolders();
    }

    public void moveEmail(int emailId, int fromId, int toId) {
        this.folders.get(fromId).removeEmail(emailId);
        this.folders.get(toId).addEmail(emailId);
        this.saveFolders();
    }

    public void copyEmail(int emailId, int folderId) {
        this.folders.get(folderId).addEmail(emailId);
        this.saveFolders();
    }

    public void deleteEmail(int emailId, int folderId) {
        this.folders.get(folderId).removeEmail(emailId);
        for (Folder folder : this.folders.values()) {
            if (folder.getName().equals("Trash") && folder.getUserId() == this.folders.get(folderId).getUserId()) {
                folder.addEmail(emailId);
                return;
            }
        }
        this.saveFolders();
    }

    public void restoreEmail(int emailId, String userId) {
        for (Folder folder : this.folders.values()) {
            if (folder.getName().equals("Inbox") && folder.getUserId().equals(userId)
                    && EmailManager.getInstance().get(emailId).isDraft() == false) {
                folder.addEmail(emailId);
                return;
            }
            if (folder.getName().equals("Draft") && folder.getUserId().equals(userId)
                    && EmailManager.getInstance().get(emailId).isDraft() == true) {
                folder.addEmail(emailId);
                return;
            }
            if (folder.getName().equals("Trash") && folder.getUserId().equals(userId)) {
                folder.removeEmail(emailId);
                return;
            }
        }
        this.saveFolders();
    }

    public ArrayList<Folder> getUserFolders(String userId) {
        ArrayList<Folder> userFolders = new ArrayList<Folder>();
        for (Folder folder : this.folders.values()) {
            if (folder.getUserId().equals(userId)) {
                userFolders.add(folder);
            }
        }
        return userFolders;
    }

    public Folder getUserFolderByName(String userId, String name) {
        for (Folder folder : this.folders.values()) {
            if (folder.getUserId().equals(userId) && folder.getName().equals(name)) {
                return folder;
            }
        }
        return null;
    }


    public Map<Object, Folder> getAll() {
        return new HashMap<Object, Folder>(this.folders);
    }
    
    public ArrayList<Folder> getAllFolders() {
        return new ArrayList<Folder>(this.folders.values());
    }

    public void setFolders(Map<Integer, Folder> folders) {
        this.folders = folders;
        this.saveFolders();
    }

    private void saveFolders() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(getAllFolders());
            Path path = Paths.get(FOLDERS_FILE_PATH);
            Files.writeString(path, json);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void loadFolders() {
        try {
            Path path = Paths.get(FOLDERS_FILE_PATH);
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
