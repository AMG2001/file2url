package tech.amg.file2url.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("file")
public class fileController {

    private File uploadDirFile;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.servlet.context-path}")
    private String storedFilesPath;

    @PostMapping
    public ResponseEntity<String> addNewFile(@RequestParam("file") MultipartFile newFile) {
        try {
            // Save the file to the server
            String fileName = newFile.getOriginalFilename();
            File file = new File(uploadDirFile, fileName);
            newFile.transferTo(file);
            // Construct the file URL
            String fileUrl = newFileUrl(fileName);
            return ResponseEntity.ok("URL of file: " + fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving file");
        }
    }

    private String newFileUrl(String fileName) {
        // Construct URL assuming the server serves static files from this directory
        return storedFilesPath + fileName;
    }

    @PostConstruct
    private void createStoredFilesDirectory() {
        try {
            // Create the uploads directory if it doesn't exist
            uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) uploadDirFile.mkdirs();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
