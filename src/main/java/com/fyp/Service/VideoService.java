package com.fyp.Service;

import com.fyp.Model.Solr.VideoIndex;
import com.fyp.Model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoService {

    Page<Video> listAllByPage(Pageable pageable);

    Video saveVideo(Video video);

    VideoIndex saveVideoIndex(VideoIndex videoIndex);

    Video findOne(Long videoId);

    Page<VideoIndex> findByTitleOrDescription(String title, String description, Pageable pageable);
}
