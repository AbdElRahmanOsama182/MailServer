package com.mail.backend.API;

import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

import io.jsonwebtoken.Jwts;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class UserController {

    @PostMapping("/login")
    public String login(@RequestBody User requser) {
                System.out.println("login");
                System.out.println(requser.getUsername());
                System.out.println(requser.getPassword());
        UserManager manager = (UserManager) ManagerFactory.getManager("UserManager");
        User user = manager.get(requser.getUsername());
        if (user != null && user.getPassword().equals(requser.getPassword())) {
            // generate jwt from user.getUsername()
            String jwtToken = Auth.getToken(requser.getUsername());
                    System.out.println("logged in");

            return jwtToken;
        }
        return null;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        System.out.println("register");
        if (user.getUsername()== null || user.getPassword() == null || user.getName() == null || user.getEmail()==null)
            return null;
        System.out.println("registered");
        user = new User(user.getUsername(), user.getPassword(), user.getName(), user.getEmail());
        UserManager manager = (UserManager) ManagerFactory.getManager("UserManager");
        
        if(manager.get(user.getUsername())!=null) return null;


        manager.add(user);

        FolderManager folderManager = (FolderManager) ManagerFactory.getManager("FolderManager");
        folderManager.createDefaultFolders(user.getUsername());
        return user;
    }

    @GetMapping("/info")
    public User info(@RequestHeader String authorization) {
        try {
            return Auth.getUser(authorization);

        } catch (Exception e) {
            return null;
        }
    }

}
