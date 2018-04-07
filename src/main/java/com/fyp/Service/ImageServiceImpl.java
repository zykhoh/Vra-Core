package com.fyp.Service;

import com.fyp.ApplicationProperties;
import com.fyp.Model.Image;
import com.fyp.Model.Solr.ImageIndex;
import com.fyp.Repository.IImageIndexRepository;
import com.fyp.Repository.IImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private IImageRepository imageRepository;
    private IImageIndexRepository imageIndexRepository;
    private ApplicationProperties applicationProperties;

    @Autowired
    public ImageServiceImpl(IImageRepository imageRepository, IImageIndexRepository imageIndexRepository, ApplicationProperties applicationProperties) {
        this.imageRepository = imageRepository;
        this.imageIndexRepository = imageIndexRepository;
        this.applicationProperties = applicationProperties;
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
    public Page<ImageIndex> findByAnnotation(List<String> annotation, Pageable pageable) {
        return imageIndexRepository.findByAnnotation(annotation, pageable);
    }

    @Override
    public Collection<String> findAllFacetByAnnotation(Pageable pageable) {

        Collection<String> annotation;
        FacetPage<ImageIndex> results = imageIndexRepository.findAllFacetByAnnotation(pageable);
        annotation = extractAnnotations(results.getFacetResultPage("annotation"));

        return annotation;
    }

    @Override
    public Map<String, Object> findByVideoIdAndFacetByAnnotation(Long videoId, Pageable pageable) {

        Map<String, Object> resultByVideo = new HashMap<>();
        FacetPage<ImageIndex> results = imageIndexRepository.findByVideoIdFacetOnAnnotation(videoId, pageable);

        ArrayList<String> annotation = extractAnnotations(results.getFacetResultPage("annotation"));

        putAnnotations(annotation, resultByVideo, results, videoId);

        return resultByVideo;
    }

    private ArrayList<String> extractAnnotations(Page<FacetFieldEntry> annotation) {
        ArrayList<String> list = new ArrayList<>();
        annotation.getContent().forEach(
                content -> list.add(content.getValue())
        );
        return list;
    }

    private void putAnnotations(ArrayList<String> annotations, Map<String, Object> map, FacetPage<ImageIndex> results, Long videoId) {
        List<ImageIndex> list = compileList(results, videoId);
        for (String annotation: annotations) {
            map.put(annotation, extractAnnotationCount(annotation, list));
        }
    }

    private List<ImageIndex> compileList(FacetPage<ImageIndex> results, Long videoId) {
        List<ImageIndex> list = new ArrayList<>();
        for (int i=0; i<results.getTotalPages(); i++) {
            FacetPage<ImageIndex> temp = imageIndexRepository.findByVideoIdFacetOnAnnotation(videoId, new PageRequest(i, applicationProperties.getPageSize()));
            list.addAll(temp.getContent());
        }

        return list;
    }

    private ArrayList<Integer> extractAnnotationCount(String annotation, List<ImageIndex> content) {
        ArrayList<Integer> list = new ArrayList<>();

        for (ImageIndex index: content) {
            if (index.getAnnotation() != null) {
                for (String annot: index.getAnnotation()) {
                    if (annot.equals(annotation)) {
                        list.add(index.getCurTime());
                    }
                }
            }
        }

        return list;
    }
}
