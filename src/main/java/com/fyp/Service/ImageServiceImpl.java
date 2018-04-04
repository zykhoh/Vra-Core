package com.fyp.Service;

import com.fyp.Model.Image;
import com.fyp.Model.Solr.ImageIndex;
import com.fyp.Repository.IImageIndexRepository;
import com.fyp.Repository.IImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private IImageRepository imageRepository;
    private IImageIndexRepository imageIndexRepository;

    @Autowired
    public ImageServiceImpl(IImageRepository imageRepository, IImageIndexRepository imageIndexRepository) {
        this.imageRepository = imageRepository;
        this.imageIndexRepository = imageIndexRepository;
    }

    @Override
    public Page<Image> listAllByPage(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public ImageIndex saveImageIndex(ImageIndex imageIndex) {
        return imageIndexRepository.save(imageIndex);
    }

    @Override
    public Image findOne(Long imageId) {
        return imageRepository.findOne(imageId);
    }

    @Override
    public List<ImageIndex> findByAnnotation(String annotation, Pageable pageable) {
        return imageIndexRepository.findByAnnotation(annotation, pageable);
    }
}
