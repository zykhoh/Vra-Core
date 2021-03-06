package com.fyp.Model;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long videoId;

    private String videoUrl;

    private String imageUrl;

    private String predictedImageUrl;

    private Integer curTime;

    public Image() {}

    public Image(Long videoId, String videoUrl, String imageUrl, String predictedImageUrl, Integer curTime) {
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
}
