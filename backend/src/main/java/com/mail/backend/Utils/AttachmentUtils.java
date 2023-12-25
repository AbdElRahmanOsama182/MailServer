package com.mail.backend.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AttachmentUtils {

    private Path fileLocation;

    @Value("${upload.base-path}")
    private String basePath = "";
    
    public File convertMFtoFile(final MultipartFile multipartFile){

        final File file = new File(multipartFile.getOriginalFilename());

        try(final FileOutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(multipartFile.getBytes());
        } catch(IOException e){
            //("Error converting Multipart file);
        }

        return file;
    }


    public String storeFile(File file, Long id, String path){

        this.fileLocation = Paths.get(basePath + path).toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.fileLocation);
        } catch (Exception e){
            System.out.println("Error Can NOT store file");
        }

        String fileName = StringUtils.cleanPath(id + "_" + file.getName());

        try{
            //handle file name 
            /*if(fileName.contains("")){

            }*/

            Path location = this.fileLocation.resolve(fileName);
            InputStream fileStream = new FileInputStream(file);
            Files.copy(fileStream, location, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;

        } catch (IOException e) {
            System.out.println("can NOT strore"+ fileName);
            return "";
        }

    }

}
