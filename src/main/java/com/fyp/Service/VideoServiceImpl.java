package com.fyp.Service;

import com.fyp.Model.Video;
import com.fyp.Repository.IVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VideoServiceImpl implements VideoService{

    private IVideoRepository videoRepository;

    @Autowired
    public VideoServiceImpl(IVideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Page<Video> listAllByPage(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    @Override
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Video findOne(Long videoId) {
        return videoRepository.findOne(videoId);
    }

}
