package com.fyp.Model.Solr;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

@SolrDocument(solrCoreName = "video")
public class VideoIndex {

    @Id
    @Field
    private Long id;

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private Date date;

    @Field
    private String videoUrl;

    public VideoIndex() {}

    public VideoIndex(Long id, String title, String description, Date date, String videoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.videoUrl = videoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}

