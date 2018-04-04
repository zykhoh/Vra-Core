package com.fyp.Controller;

import com.fyp.ApplicationProperties;
import com.fyp.Model.Solr.VideoIndex;
import com.fyp.Model.Video;
import com.fyp.Service.Classifier.ObjectDetector;
import com.fyp.Service.ImageService;
import com.fyp.Service.Processor.Indexing;
import com.fyp.Service.SearchTermParserService;
import com.fyp.Service.StorageService;
import com.fyp.Service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/api/videos")
public class VideoController {

    private ApplicationProperties applicationProperties;

    private VideoService videoService;

    private StorageService storageService;

    private SearchTermParserService searchTermParserService;

    private ExecutorService executorService;

    private ObjectDetector objectDetector;

    private ImageService imageService;

    @Autowired
    public VideoController(VideoService videoService, StorageService storageService, SearchTermParserService searchTermParserService, ApplicationProperties applicationProperties, ObjectDetector objectDetector, ImageService imageService) {
        this.applicationProperties = applicationProperties;
        this.videoService = videoService;
        this.storageService = storageService;
        this.searchTermParserService = searchTermParserService;
        this.executorService = Executors.newFixedThreadPool(applicationProperties.getNoOfThread());
        this.objectDetector = objectDetector;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<Video>> videoList(Pageable pageable){

        Page<Video> videos = videoService.listAllByPage(pageable);

        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<VideoIndex> addVideo(@RequestParam("videoFile") MultipartFile videoFile, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        String temp = storageService.storeVideo(videoFile);
        String videoPath = "/videos/" + temp;
        String videoFolder = temp.substring(0, temp.lastIndexOf("/"));
        String imageFolder = "/videos/" + storageService.createRawImageFolders(videoFolder);
        String predictedFolder = "/videos/" + storageService.createPredictedImageFolders(videoFolder);

        Video video = new Video(title, description, date, videoPath, imageFolder, predictedFolder);
        Video vid = videoService.saveVideo(video);

        executorService.execute(new Indexing(applicationProperties, video, objectDetector, imageService));

        VideoIndex videoIndex = new VideoIndex(vid.getId(), vid.getTitle().toLowerCase(), vid.getDescription().toLowerCase(), vid.getDate());
        VideoIndex vidIndex = videoService.saveVideoIndex(videoIndex);

        return new ResponseEntity<>(vidIndex, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> video(@PathVariable("id") Long videoId){

        Video video = videoService.findOne(videoId);

        if (video != null) {
            return new ResponseEntity<>(video, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/search")
    public ResponseEntity<Page<VideoIndex>> findByTitleContainsOrDescriptionContains(@RequestParam(value = "searchTerm", required = false) String searchTerm, Pageable pageable) {

        Page<VideoIndex> videoIndex;
        String[] words = searchTermParserService.tokenizeSearchTerm(searchTerm);
        List<String> wordList = Arrays.asList(words);

        if (searchTermParserService.hasDate(words)) {
            List<LocalDate> dateList = searchTermParserService.trimSearchTerms(wordList);
            videoIndex = videoService.findByDateIsNear(dateList, pageable);
        }else {
            wordList = searchTermParserService.removePunctuationMark(wordList);
            videoIndex = videoService.findByTitleContainsOrDescriptionContains(wordList, wordList, pageable);
        }


        return new ResponseEntity<>(videoIndex, HttpStatus.OK);

    }

}
