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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Indexing implements Runnable {

    private String FFMPEG_ROOT;

    private String FILES_ROOT;

    private Video video;

    private String ImageInputRoot = null;

    private String ImageOutputRoot = null;

    private ObjectDetector objectDetector;

    private ImageService imageService;

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
        split();
        extract();
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

            Map<String, Object> result = objectDetector.detect(inputFilePath, ImageOutputRoot);

            for(Map.Entry m: result.entrySet()){
                if (m.getKey()=="recognitions") {
                    annotation = (ArrayList<String>) m.getValue();
                }
                if (m.getKey()=="labeledFilePath") {
                    outputFilePath = (String) m.getValue();
                }
            }

            Image newImage = new Image(video.getVideoUrl(), inputFilePath, outputFilePath, Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf("."))));

            Image image = imageService.saveImage(newImage);
            ImageIndex imageIndex = new ImageIndex(image.getId(), image.getVideoUrl(), image.getImageUrl(), image.getPredictedImageUrl(), image.getCurTime());

            if (!annotation.isEmpty()) {
                imageIndex.setAnnotation(annotation);
            }

            imageService.saveImageIndex(imageIndex);
        }
    }
}
