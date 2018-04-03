package com.fyp.Model.Solr;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;

@SolrDocument(solrCoreName = "image")
public class ImageIndex {

    @Id
    @Field
    private Long id;

    @Field
    private String videoUrl;

    @Field
    private String imageUrl;

    @Field
    private String predictedImageUrl;

    @Field
    private Integer curTime;

    @Field
    private ArrayList<String> annotation;

    public ImageIndex() {}

    public ImageIndex(Long id, String videoUrl, String imageUrl, String predictedImageUrl, Integer curTime) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.predictedImageUrl = predictedImageUrl;
        this.curTime = curTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPredictedImageUrl() {
        return predictedImageUrl;
    }

    public void setPredictedImageUrl(String predictedImageUrl) {
        this.predictedImageUrl = predictedImageUrl;
    }

    public Integer getCurTime() {
        return curTime;
    }

    public void setCurTime(Integer curTime) {
        this.curTime = curTime;
    }

    public ArrayList<String> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ArrayList<String> annotation) {
        this.annotation = annotation;
    }
}
