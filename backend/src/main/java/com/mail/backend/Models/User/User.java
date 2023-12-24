package com.mail.backend.Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class User implements Serializable {
    
    private String username;
    private String password;
    private String name;

    
    
    private User() {

    }

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserModel [username=" + username + ", password=" + password + ", name=" + name + "]";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", this.username);
        map.put("password", this.password);
        map.put("name", this.name);
        return map;
    }
    
}