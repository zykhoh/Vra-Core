package com.fyp.Service.Processor;

import com.fyp.ApplicationProperties;
import com.fyp.Model.Image;
import com.fyp.Model.Solr.ImageIndex;
import com.fyp.Model.Video;
import com.fyp.Service.Classifier.ObjectDetector;
import com.fyp.Service.ImageService;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
import net.semanticmetadata.lire.imageanalysis.features.LireFeature;
import net.semanticmetadata.lire.imageanalysis.features.global.*;
import net.semanticmetadata.lire.indexers.hashing.BitSampling;
import net.semanticmetadata.lire.solr.indexing.ParallelSolrIndexer;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Indexing implements Runnable {

    private String FFMPEG_ROOT;

    private String FILES_ROOT;

    private LireFeature[] listOfFeatures = new LireFeature[]{
            new ColorLayout(), new PHOG(), new EdgeHistogram(), new JCD(), new OpponentHistogram()
    };

    private static HashMap<Class, String> classToPrefix = new HashMap<Class, String>(5);

    static {
        classToPrefix.put(ColorLayout.class, "cl");
        classToPrefix.put(EdgeHistogram.class, "eh");
        classToPrefix.put(PHOG.class, "ph");
        classToPrefix.put(OpponentHistogram.class, "oh");
        classToPrefix.put(JCD.class, "jc");
    }

    private Video video;

    private String ImageInputRoot = null;

    private String ImageOutputRoot = null;

    private ObjectDetector objectDetector;

    private ImageService imageService;

    private final static Logger LOGGER = LoggerFactory.getLogger(Indexing.class);

    @Autowired
    public Indexing(final ApplicationProperties applicationProperties, Video video, ObjectDetector objectDetector, ImageService imageService) {
        this.video = video;
        this.FFMPEG_ROOT = applicationProperties.getFfmpegDir();
        this.FILES_ROOT = applicationProperties.getVraDir();
        this.ImageInputRoot = FILES_ROOT + video.getImageFolder();
        this.ImageOutputRoot = FILES_ROOT + video.getPredictedFolder() + "/";
        this.objectDetector = objectDetector;
        this.imageService = imageService;
    }

    @Override
    public void run() {
        Long start = System.currentTimeMillis();
        split();
        extract();
        Long end = System.currentTimeMillis();

        System.out.println("Took: " + ((end - start) / 1000) + "s");
    }

    private void split() {
        FFmpeg ffmpeg = null;
        try {
            ffmpeg = new FFmpeg(FFMPEG_ROOT);

            FFmpegBuilder builder =
                    new FFmpegBuilder()
                            .setInput(FILES_ROOT + video.getVideoUrl())
                            .addOutput(FILES_ROOT + video.getImageFolder() + "/%06d.jpg")
                            .setVideoFrameRate(1)
                            .setFormat("image2")
                            .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
            executor.createJob(builder).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extract() {
        File folder=new File(ImageInputRoot);
        File[] files=folder.listFiles();

        for (File file : files) {
            String inputFilePath = file.getPath();
            String outputFilePath = null;
            ArrayList<String> annotation = new ArrayList<String>();
            BufferedImage img = null;

            try {
                img = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Object> result = objectDetector.detect(inputFilePath, ImageOutputRoot);

            for(Map.Entry m: result.entrySet()){
                if (m.getKey()=="recognitions") {
                    annotation = (ArrayList<String>) m.getValue();
                }
                if (m.getKey()=="labeledFilePath") {
                    outputFilePath = (String) m.getValue();
                }
            }

            Image newImage = new Image(video.getId(), video.getVideoUrl(), inputFilePath, outputFilePath, Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf("."))));

            Image image = imageService.saveImage(newImage);
            ImageIndex imageIndex = new ImageIndex(image.getId(), video.getId(), image.getVideoUrl(), image.getImageUrl(), image.getPredictedImageUrl(), image.getCurTime());

            if (!annotation.isEmpty()) {
                imageIndex.setAnnotation(annotation);
            }

            for (int i = 0; i < listOfFeatures.length; i++) {

                LireFeature feature = listOfFeatures[i];
                ((GlobalFeature) feature).extract(img);
                try {
                    BitSampling.readHashFunctions();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (classToPrefix.get(feature.getClass()) == "cl") {

                    System.out.println("Extracting ColorLayout...");
                    imageIndex.setCl_hi(Base64.encodeBase64String(feature.getByteArrayRepresentation()));
                    imageIndex.setCl_ha(ParallelSolrIndexer.arrayToString(BitSampling.generateHashes(((GlobalFeature) feature).getFeatureVector())));

                }else if (classToPrefix.get(feature.getClass()) == "eh") {

                    System.out.println("Extracting EdgeHistogram...");
                    imageIndex.setEh_hi(Base64.encodeBase64String(feature.getByteArrayRepresentation()));
                    imageIndex.setEh_ha(ParallelSolrIndexer.arrayToString(BitSampling.generateHashes(((GlobalFeature) feature).getFeatureVector())));

                }else if (classToPrefix.get(feature.getClass()) == "ph") {

                    System.out.println("Extracting PHOG...");
                    imageIndex.setPh_hi(Base64.encodeBase64String(feature.getByteArrayRepresentation()));
                    imageIndex.setPh_ha(ParallelSolrIndexer.arrayToString(BitSampling.generateHashes(((GlobalFeature) feature).getFeatureVector())));

                }else if (classToPrefix.get(feature.getClass()) == "oh") {

                    System.out.println("Extracting OpponentHistogram...");
                    imageIndex.setOh_hi(Base64.encodeBase64String(feature.getByteArrayRepresentation()));
                    imageIndex.setOh_ha(ParallelSolrIndexer.arrayToString(BitSampling.generateHashes(((GlobalFeature) feature).getFeatureVector())));

                }else {

                    System.out.println("Extracting JCD...");
                    imageIndex.setJc_hi(Base64.encodeBase64String(feature.getByteArrayRepresentation()));
                    imageIndex.setJc_ha(ParallelSolrIndexer.arrayToString(BitSampling.generateHashes(((GlobalFeature) feature).getFeatureVector())));

                }

            }

            imageService.saveImageIndex(imageIndex);
        }
    }
}
