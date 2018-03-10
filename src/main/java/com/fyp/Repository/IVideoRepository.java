package com.fyp.Repository;

import com.fyp.Model.Video;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IVideoRepository extends PagingAndSortingRepository<Video, Long> {

}
