package com.fyp.Service;

import com.fyp.Model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoService {

    Page<Video> listAllByPage(Pageable pageable);

    Video saveVideo(Video video);

    Video findOne(Long videoId);
}
