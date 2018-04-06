package com.fyp.Controller;

import com.fyp.ApplicationProperties;
import com.fyp.Model.Solr.ImageIndex;
import com.fyp.Model.Solr.VideoIndex;
import com.fyp.Service.ImageService;
import com.fyp.Service.SearchTermParserService;
import com.fyp.Service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/search")
public class SearchController {

    private SearchTermParserService searchTermParserService;

    private VideoService videoService;

    private ImageService imageService;

    private int noOfPage, pageSize;

    @Autowired
    public SearchController(SearchTermParserService searchTermParserService, VideoService videoService, ImageService imageService, final ApplicationProperties applicationProperties) {
        this.searchTermParserService = searchTermParserService;
        this.videoService = videoService;
        this.imageService = imageService;
        this.noOfPage = applicationProperties.getNoOfPage();
        this.pageSize = applicationProperties.getPageSize();
    }

    @PostMapping("/video")
    public ResponseEntity<Page<VideoIndex>> videoSearch(@RequestParam(value = "searchTerm", required = false) String searchTerm, Pageable pageable) {

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

    @PostMapping("/image")
    public ResponseEntity<Page<ImageIndex>> imageSearch(@RequestParam("annotation") String searchTerm) {

        Page<ImageIndex> list = imageService.findByAnnotation(searchTerm, new PageRequest(noOfPage, pageSize));

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/facet")
    public ResponseEntity<Collection<String>> facetAnnotation() {
        Collection<String> annotations = imageService.findAllFacetByAnnotation(new PageRequest(noOfPage, pageSize));

        return new ResponseEntity<>(annotations, HttpStatus.OK);
    }

    @GetMapping("/facet/{id}")
    public ResponseEntity<Map<String, Object>> findImageIndexByIdAndFacetByAnnotation(@PathVariable("id") Long videoId) {
        Map<String, Object> annotations = imageService.findByVideoIdAndFacetByAnnotation(videoId, new PageRequest(noOfPage, pageSize));

        return new ResponseEntity<>(annotations, HttpStatus.OK);
    }


}
