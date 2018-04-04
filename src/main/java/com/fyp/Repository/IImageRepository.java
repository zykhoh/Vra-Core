package com.fyp.Repository;

import com.fyp.Model.Image;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IImageRepository extends PagingAndSortingRepository<Image, Long> {
}
