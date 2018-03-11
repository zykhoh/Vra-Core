package com.fyp.Service;

import com.fyp.Model.Solr.VideoIndex;
import com.fyp.Model.Video;
import com.fyp.Repository.IVideoIndexRepository;
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
    private IVideoIndexRepository videoIndexRepository;

    @Autowired
    public VideoServiceImpl(IVideoRepository videoRepository, IVideoIndexRepository videoIndexRepository) {
        this.videoRepository = videoRepository;
        this.videoIndexRepository = videoIndexRepository;
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
    public VideoIndex saveVideoIndex(VideoIndex videoIndex) {
        return videoIndexRepository.save(videoIndex);
    }

    @Override
    public Video findOne(Long videoId) {
        return videoRepository.findOne(videoId);
    }

    @Override
    public Page<VideoIndex> findByTitleOrDescription(String title, String description, Pageable pageable) {
        return videoIndexRepository.findByTitleOrDescription(title, description, pageable);
    }

}
