package com.mail.backend.API;

import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Models.User.User;
import com.mail.backend.Utils.Auth;

import io.jsonwebtoken.Jwts;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mail.backend.Managers.EmailManager;
import com.mail.backend.Managers.FolderManager;
import com.mail.backend.Managers.ManagerFactory;


@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class FolderController {

    @GetMapping("/folders/{id}")
    public Folder read(@RequestHeader String authorization,@PathVariable("id") String id){
        FolderManager folderManager =(FolderManager)ManagerFactory.getManager("FolderManager");
        User user= Auth.getUser(authorization);
        Folder folder = folderManager.get(id);
        if(folder.getUserId() != user.getUsername()) return null;

        return folder;
    }
    
    @PostMapping("/folders")
    public Folder create(@RequestHeader String authorization, @RequestBody Folder folder){
        User user=Auth.getUser(authorization);

        if(user==null|| folder.getName()==null) return null;
        folder.setUserId(user.getUsername());

        FolderManager folderManager =(FolderManager) ManagerFactory.getManager("FolderManager");
        folderManager.add(folder);

        return folder;
    }
}
