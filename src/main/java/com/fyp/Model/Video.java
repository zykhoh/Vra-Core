package com.fyp.Model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank
    private String videoUrl;

    @NotBlank
    private String imageFolder;

    @NotBlank
    private String predictedFolder;

    public Video() {
    }

    public Video(String title, String description, Date date, String videoUrl, String imageFolder, String predictedFolder) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.videoUrl = videoUrl;
        this.imageFolder = imageFolder;
        this.predictedFolder = predictedFolder;
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

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public String getPredictedFolder() {
        return predictedFolder;
    }

    public void setPredictedFolder(String predictedFolder) {
        this.predictedFolder = predictedFolder;
    }
}