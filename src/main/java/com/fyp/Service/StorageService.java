package com.fyp.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.fyp.ApplicationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private final Path videoRootLocation;
    private final Path imageRootLocation;
    private final Path uploadDir;
    private final Path predictedImageRootLocation;
    private ApplicationProperties applicationProperties;

    public StorageService(final ApplicationProperties applicationProperties) {
        this.videoRootLocation = Paths.get(applicationProperties.getVideoUploadDir());
        this.imageRootLocation = Paths.get(applicationProperties.getImageUploadDir());
        this.uploadDir = Paths.get(applicationProperties.getUploadDir());
        this.predictedImageRootLocation = Paths.get(applicationProperties.getOutputDir());
        this.applicationProperties = applicationProperties;
    }

    public String storeVideo(MultipartFile file){
        try {
            String newFolderName = String.valueOf(UUID.randomUUID());
            String newFileName = newFolderName + ".mp4";
            Path videoPath = createVideoFolders(newFolderName);
            Files.copy(file.getInputStream(), videoPath.resolve(newFileName));

            return newFolderName + "/" + newFileName;
        } catch (Exception e) {
            throw new RuntimeException("This video already existed!!");
        }
    }

    public String storeImage(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.uploadDir.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            return file.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("This image already existed!!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(videoRootLocation.toFile());
        FileSystemUtils.deleteRecursively(imageRootLocation.toFile());
        FileSystemUtils.deleteRecursively(uploadDir.toFile());
        FileSystemUtils.deleteRecursively(predictedImageRootLocation.toFile());
    }

    public void init() {
        try {
            if (!Files.exists(videoRootLocation)) {
                Files.createDirectory(videoRootLocation);
            }
            if (!Files.exists(imageRootLocation)) {
                Files.createDirectory(imageRootLocation);
            }
            if (!Files.exists(uploadDir)) {
                Files.createDirectory(uploadDir);
            }
            if (!Files.exists(predictedImageRootLocation)) {
                Files.createDirectory(predictedImageRootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize video or/and image storage!");
        }
    }

    private Path createVideoFolders(String newFolderName) {
        try {
            Path videoFolder = Paths.get(applicationProperties.getVideoUploadDir() + newFolderName);
            if (!Files.exists(videoFolder)) {
                Files.createDirectory(videoFolder);
            }
            return videoFolder;
        } catch (IOException e) {
            throw new RuntimeException("Could not create video folder!");
        }
    }

    public String createRawImageFolders(String newFolderName) {
        try {
            String url = newFolderName + "/" + applicationProperties.getRawImageFolder();
            Path rawImageFolder = Paths.get(applicationProperties.getVideoUploadDir() + url);
            if (!Files.exists(rawImageFolder)) {
                Files.createDirectory(rawImageFolder);
            }

            return url;
        } catch (IOException e) {
            throw new RuntimeException("Could not create raw image folder for the video!");
        }
    }

    public String createPredictedImageFolders(String newFolderName) {
        try {
            String url = newFolderName + "/" + applicationProperties.getPredictedImageFolder();
            Path predictedImageFolder = Paths.get(applicationProperties.getVideoUploadDir() + url);
            if (!Files.exists(predictedImageFolder)) {
                Files.createDirectory(predictedImageFolder);
            }

            return url;
        } catch (IOException e) {
            throw new RuntimeException("Could not create predicted image folder for the video!");
        }
    }
}
