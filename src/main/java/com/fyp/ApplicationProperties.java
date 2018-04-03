package com.fyp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String graph;
    private String label;
    private String outputDir;
    private String uploadDir;
    private Integer imageSize;
    private Float imageMean;
    private String videoUploadDir;
    private String imageUploadDir;
    private String ffmpegDir;
    private String rawImageFolder;
    private String predictedImageFolder;
    private int noOfThread;
    private String vraDir;

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public Integer getImageSize() {
        return imageSize;
    }

    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    public Float getImageMean() {
        return imageMean;
    }

    public void setImageMean(Float imageMean) {
        this.imageMean = imageMean;
    }

    public String getVideoUploadDir() {
        return videoUploadDir;
    }

    public void setVideoUploadDir(String videoUploadDir) {
        this.videoUploadDir = videoUploadDir;
    }

    public String getImageUploadDir() {
        return imageUploadDir;
    }

    public void setImageUploadDir(String imageUploadDir) {
        this.imageUploadDir = imageUploadDir;
    }

    public String getFfmpegDir() {
        return ffmpegDir;
    }

    public void setFfmpegDir(String ffmpegDir) {
        this.ffmpegDir = ffmpegDir;
    }

    public String getRawImageFolder() {
        return rawImageFolder;
    }

    public void setRawImageFolder(String rawImageFolder) {
        this.rawImageFolder = rawImageFolder;
    }

    public String getPredictedImageFolder() {
        return predictedImageFolder;
    }

    public void setPredictedImageFolder(String predictedImageFolder) {
        this.predictedImageFolder = predictedImageFolder;
    }

    public int getNoOfThread() {
        return noOfThread;
    }

    public void setNoOfThread(int noOfThread) {
        this.noOfThread = noOfThread;
    }

    public String getVraDir() {
        return vraDir;
    }

    public void setVraDir(String vraDir) {
        this.vraDir = vraDir;
    }
}
