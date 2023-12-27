package com.mail.backend.Managers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ws.mime.Attachment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.backend.Models.Afile.Afile;
import com.mail.backend.Models.Email.Email;
import com.mail.backend.Models.Folder.Folder;
import com.mail.backend.Utils.AttachmentUtils;

@RestController
@RequestMapping("/attachment")
public class AttachmentManager implements ManagerInterface<Afile>{

    private static final String AFILES_FILE_PATH = "src/main/java/com/mail/backend/data/Afiles.json";
    private static AttachmentManager instance;
    public Map<Integer, Afile> afiles = new HashMap<Integer, Afile>();
    private int nextId = 0;


    private AttachmentManager(){
    }

    public static synchronized AttachmentManager getInstance() {
        if (instance == null) {
            instance = new AttachmentManager();
            instance.loadAfiles();
        }
        return instance;
    }

    @Autowired
    private AttachmentUtils AttachmentUtils;
    
    public Afile uploadFiles(MultipartFile[] files){
        ArrayList<Path> paths = new ArrayList<>();
        for(MultipartFile file  : files){
            Path location = AttachmentUtils.storeFile(AttachmentUtils.convertMFtoFile(file));
            paths.add(location);
        }
        Afile afile = new Afile(paths);
        return addAfile(afile);
    }


  

    @Override
    public Afile get(Object id) {
        return this.afiles.get((int) id);
    }

    @Override
    public Map<Object, Afile> getAll() {
        return new HashMap<Object, Afile>(this.afiles);
    }

    @Override
    public Afile add(Afile t) {
        return this.addAfile(t);
    }

    @Override
    public void remove(Object id) {
        this.afiles.remove(id);
    }


    public int getNextId() {
        return this.nextId;
    }

    public Afile addAfile(Afile afile){
        if(afile == null){
            return null;
        }
        afile.setId(this.nextId);
        this.afiles.put(this.nextId, afile);
        this.nextId++;
        saveAfiles();
        return afile;
    }

    public ArrayList<Afile> getAllAfiles() {
        return new ArrayList<Afile>(this.afiles.values());
    }
    

    public void saveAfiles() {
        System.out.println("Saving Afiles");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(this.getAllAfiles());
            Path path = Paths.get(AFILES_FILE_PATH);
            System.out.println(json);
            Files.writeString(path, json);
            System.out.println("Saved Afiles");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadAfiles() {
        try {
            Path path = Paths.get(AFILES_FILE_PATH);
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Afile> afiles = mapper.readValue(new File(AFILES_FILE_PATH),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, Afile.class));
            for (Afile afile : afiles) {
                this.afiles.put(afile.getId(), afile);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
