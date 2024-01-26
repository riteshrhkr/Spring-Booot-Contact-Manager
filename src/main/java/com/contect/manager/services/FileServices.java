package com.contect.manager.services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServices {
    private ResourceLoader resourceLoader;
    static private String staticDir;

    public FileServices(ResourceLoader resourceLoader) throws IOException {
        try {
            this.resourceLoader = resourceLoader;
            URI uri = resourceLoader.getResource("classpath:/static").getURI();
            staticDir = new File(uri).getPath();
        } catch (Exception e) {
            System.out.println("Static Dir - " + staticDir);
            System.out.println(e.getMessage());
        }

        /* or */
        // try
        // {
        // String img = new ClassPathResource("static").getFile().getAbsolutePath();
        // System.out.println(img);
        // }
        // catch (IOException e)
        // {
        // e.printStackTrace();
        // }
    }

    /**
     * Saves file to specified directory.
     *
     * @param file           MultipartFile to be saved.
     * @param dirAfterStatic Directory after static. eg: "/img/users/{userName}"
     * 
     * @return Name of the saved file or null
     */
    public String saveFile(MultipartFile file, String dirAfterStatic) {
        try {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String uploadDir = staticDir + dirAfterStatic;

                // Create directories if not exists
                Files.createDirectories(Path.of(uploadDir));

                Path filePath = Path.of(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File saved at: " + filePath);
                return fileName;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
    * Check if the given content type is an image type.
    *
    * @param  contantType  the content type to be checked
    * @return              true if the content type is an image type (png, jpg, jpeg), false otherwise
    */
    public static boolean isImage(String contantType) {
        if (contantType == null) {
            return false;
        } else if (contantType.equals("image/png") || contantType.equals("image/jpg")
                || contantType.equals("image/jpeg")) {
            return true;
        } else {
            return false;
        }

    }

    /**
    * Deletes the file at the specified path.
    *
    * @param  pathAfterStatic   the path after the static directory eg: "/img/users/{userName}/img.jpg"
    * @return                  true if the file is successfully deleted, false otherwise
    */
    static public boolean deleteFile(String pathAfterStatic) {
        try {

            Path filePath = Path.of(staticDir, pathAfterStatic);
            Files.delete(filePath);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
    * Update a file in the specified directory after the static directory.
    *
    * @param  file              the file to be updated
    * @param  dirAfterStatic    the directory after the static directory
    * @param  oldFileName       the old file name
    * @return                   the new file name after update
    */
    public String updateFile(MultipartFile file, String dirAfterStatic, String oldFileName) {
        if (file.isEmpty()) {
            return null;
        } else {
            try {
                // if oldFileName is null then it throw an error
                FileServices.deleteFile(dirAfterStatic + "/" + oldFileName);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            String fileName = saveFile(file, dirAfterStatic);
            System.out.println("FileName = " + fileName);
            return fileName;
        }
    }
}
