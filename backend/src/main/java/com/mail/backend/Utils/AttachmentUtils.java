package com.mail.backend.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentUtils {

    private static Path fileLocation;

    @Value("${upload.base-path}")
    private static String basePath = "backend\\src\\main\\resources\\static\\";

    public static File convertMFtoFile(final MultipartFile multipartFile) {

        final File file = new File(multipartFile.getOriginalFilename());

        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            // ("Error converting Multipart file);
        }

        return file;
    }

    public static Path storeFile(File file) {

        fileLocation = Paths.get(basePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileLocation);
        } catch (Exception e) {

            System.out.println("Error Can NOT store file");
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = StringUtils.cleanPath(file.getName());
        fileName = timestamp + "_" + fileName;

        try {
            // handle file name
            /*
             * if(fileName.contains("")){
             * 
             * }
             */

            Path location = fileLocation.resolve(fileName);
            Files.move(Paths.get(file.getPath()), location, StandardCopyOption.REPLACE_EXISTING);

            return location;


        } catch (IOException e) {
            System.out.println("can NOT strore" + fileName);
            return null;
        }
    }

    public static Resource getFile(Path path) {
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {

            System.err.println(e.getMessage());
        }

        return null;
    }

}
