package com.fyp.Service;

import com.fyp.Model.Image;
import com.fyp.Model.Solr.ImageIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageService {

    Page<Image> listAllByPage(Pageable pageable);

    Image saveImage(Image image);

    ImageIndex saveImageIndex(ImageIndex imageIndex);

    Image findOne(Long imageId);

    List<ImageIndex> findByAnnotation(String annotation, Pageable pageable);

}
