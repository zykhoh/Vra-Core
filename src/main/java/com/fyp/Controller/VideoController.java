package com.fyp.Controller;

import com.fyp.Model.Solr.VideoIndex;
import com.fyp.Model.Video;
import com.fyp.Service.StorageService;
import com.fyp.Service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Controller
@RequestMapping("/api")
public class VideoController {

    private static final String VIDEO_ROOT = "/Users/zykhoh/Downloads/Vra/";

    private VideoService videoService;

    private StorageService storageService;

    @Autowired
    public VideoController(VideoService videoService, StorageService storageService) {
        this.videoService = videoService;
        this.storageService = storageService;
    }

    @GetMapping("/videos")
    public ResponseEntity<Page<Video>> videoList(Pageable pageable){

        Page<Video> videos = videoService.listAllByPage(pageable);

        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @PostMapping("/videos/add")
    public ResponseEntity<VideoIndex> addVideo(@RequestParam("videoFile") MultipartFile videoFile, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        Video video = new Video(title, description, date, VIDEO_ROOT + videoFile.getOriginalFilename());

        storageService.store(videoFile);
        Video vid = videoService.saveVideo(video);
        VideoIndex videoIndex = new VideoIndex(vid.getId(), vid.getTitle(), vid.getDescription(), vid.getDate());
        VideoIndex vidIndex = videoService.saveVideoIndex(videoIndex);

        return new ResponseEntity<>(vidIndex, HttpStatus.ACCEPTED);
    }

    @GetMapping("/videos/{id}")
    public ResponseEntity<Video> video(@PathVariable("id") Long videoId){

        Video video = videoService.findOne(videoId);

        if (video != null) {
            return new ResponseEntity<>(video, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/videos/search")
    public ResponseEntity<VideoIndex> findByTitleOrDescription(String searchTerm){

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
