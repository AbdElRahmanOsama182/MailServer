package com.mail.backend.Models.Afile;

import java.nio.file.Path;
import java.util.ArrayList;

public class Afile {
    //private  int nextId = 0;
    private int id;
    private ArrayList<Path> paths;

/*private synchronized int getNextId() {
        return nextId++;
    }*/
    
    public Afile() {
    }

    public Afile(ArrayList<Path> paths){
        //this.id = getNextId();
        this.paths = paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public ArrayList<Path> getPaths() {
        return paths;
    }

    @Override
    public String toString() {
        return "Afile [id=" + id + ", paths=" + paths + "]";
    }

    
    
}
