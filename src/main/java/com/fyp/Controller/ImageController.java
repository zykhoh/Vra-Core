package com.fyp.Controller;

import com.fyp.ApplicationProperties;
import com.fyp.Service.Classifier.ObjectDetector;
import com.fyp.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/api/images")
public class ImageController {

    private StorageService storageService;
    private ObjectDetector objectDetector;
    private String IMAGE_ROOT;

    @Autowired
    public ImageController(StorageService storageService, ObjectDetector objectDetector, final ApplicationProperties applicationProperties) {
        this.storageService = storageService;
        this.objectDetector = objectDetector;
        this.IMAGE_ROOT = applicationProperties.getUploadDir();
    }

    @PostMapping("detect")
    public ResponseEntity<Map<String, Object>> detect(@RequestParam("Image") MultipartFile imageFile) {
        String originalImagePath = IMAGE_ROOT + storageService.storeImage(imageFile);
        Map<String, Object> result = objectDetector.detect(originalImagePath);
        result.put("originalImagePath", originalImagePath);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
