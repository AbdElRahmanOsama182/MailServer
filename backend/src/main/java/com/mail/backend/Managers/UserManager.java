package com.mail.backend.Managers;

import java.util.HashMap;
import java.util.Map;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.FileManager;
public class UserManager {
    private static UserManager instance = null;
    private Map<String, User> users = new HashMap<String, User>();

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
            instance.users = FileManager.readObjectFromFile("users.dat");

            if(instance.users==null){
                System.out.println("users.dat not found, creating new file");
                instance.users = new HashMap<String, User>();
                FileManager.saveObjectToFile(instance.users, "users.dat");
            }
        }
        return instance;
    }

    
    public User getUser(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        FileManager.saveObjectToFile(users, "users.dat");
    }

    public void removeUser(String username) {
        users.remove(username);
        FileManager.saveObjectToFile(users, "users.dat");
    }

    public Map<String, User> getUsers() {
        return users;
    }




}
