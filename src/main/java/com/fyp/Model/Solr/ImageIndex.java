package com.fyp.Model.Solr;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;

@SolrDocument(solrCoreName = "image")
public class ImageIndex {

    @Id
    @Field
    private Long id;

    @Field
    private Long videoId;

    @Field
    private String videoUrl;

    @Field
    private String imageUrl;

    @Field
    private String predictedImageUrl;

    @Field
    private Integer curTime;

    @Field
    @Indexed
    private ArrayList<String> annotation;

    //    ColourLayout

    @Field
    private String cl_hi;

    @Field
    private String cl_ha;

//    EdgeHistogram

    @Field
    private String eh_hi;

    @Field
    private String eh_ha;

//    PHOG

    @Field
    private String ph_hi;

    @Field
    private String ph_ha;

//    OpponentHistogram

    @Field
    private String oh_hi;

    @Field
    private String oh_ha;

//    JCD

    @Field
    private String jc_hi;

    @Field
    private String jc_ha;

    public ImageIndex() {}

    public ImageIndex(Long id, Long videoId, String videoUrl, String imageUrl, String predictedImageUrl, Integer curTime) {
        this.id = id;
        this.videoId = videoId;
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

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
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

    public String getCl_hi() {
        return cl_hi;
    }

    public void setCl_hi(String cl_hi) {
        this.cl_hi = cl_hi;
    }

    public String getCl_ha() {
        return cl_ha;
    }

    public void setCl_ha(String cl_ha) {
        this.cl_ha = cl_ha;
    }

    public String getEh_hi() {
        return eh_hi;
    }

    public void setEh_hi(String eh_hi) {
        this.eh_hi = eh_hi;
    }

    public String getEh_ha() {
        return eh_ha;
    }

    public void setEh_ha(String eh_ha) {
        this.eh_ha = eh_ha;
    }

    public String getPh_hi() {
        return ph_hi;
    }

    public void setPh_hi(String ph_hi) {
        this.ph_hi = ph_hi;
    }

    public String getPh_ha() {
        return ph_ha;
    }

    public void setPh_ha(String ph_ha) {
        this.ph_ha = ph_ha;
    }

    public String getOh_hi() {
        return oh_hi;
    }

    public void setOh_hi(String oh_hi) {
        this.oh_hi = oh_hi;
    }

    public String getOh_ha() {
        return oh_ha;
    }

    public void setOh_ha(String oh_ha) {
        this.oh_ha = oh_ha;
    }

    public String getJc_hi() {
        return jc_hi;
    }

    public void setJc_hi(String jc_hi) {
        this.jc_hi = jc_hi;
    }

    public String getJc_ha() {
        return jc_ha;
    }

    public void setJc_ha(String jc_ha) {
        this.jc_ha = jc_ha;
    }
}
