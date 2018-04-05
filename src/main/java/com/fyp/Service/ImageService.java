package com.fyp.Service;

import com.fyp.Model.Image;
import com.fyp.Model.Solr.ImageIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ImageService {

    Page<Image> listAllByPage(Pageable pageable);

    Image saveImage(Image image);

    ImageIndex saveImageIndex(ImageIndex imageIndex);

    Image findOne(Long imageId);

    Page<ImageIndex> findByAnnotation(String annotation, Pageable pageable);

    Collection<String> findAllFacetByAnnotation(Pageable pageable);

    Map<String, Object> findByVideoIdAndFacetByAnnotation(Long videoId, Pageable pageable);

}
