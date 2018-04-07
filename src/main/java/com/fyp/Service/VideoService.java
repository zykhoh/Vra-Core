package com.fyp.Service;

import com.fyp.Model.Solr.VideoIndex;
import com.fyp.Model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface VideoService {

    Page<Video> listAllByPage(Pageable pageable);

    Video saveVideo(Video video);

    VideoIndex saveVideoIndex(VideoIndex videoIndex);

    Video findOne(Long videoId);

    Page<VideoIndex> findByTitleLikeOrDescriptionLike(List<String> title, List<String> description, Pageable pageable);

    Page<VideoIndex> findByDateIsNear(List<LocalDate> dates, Pageable pageable);
}
