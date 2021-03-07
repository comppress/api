package org.example.API.model;

import javax.persistence.*;

@Entity
public class Content {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name="link")
    private String link;
    @Column(name="title")
    private String title;
    @Column(name="image_link")
    private String imageLink;
    @Column(name="source")
    private String source;
    @Column(name="description")
    private String description;
    @Column(name="creation_date", insertable=false)
    private String creationDate;
    @Column(name="category")
    private String category;
    @Column(name="rss_feed_id")
    private Long rssFeedId;
    @Column(name="count_rating")
    private Long countRating;
    @Column(name="average_rating")
    private Double averageRating;
    @Column(name="sum_rating")
    private Double sumRating;

    public void setSumRating(Double sumRating) {
        this.sumRating = sumRating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getSumRating() {
        return sumRating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCountRating(Long countRating) {
        this.countRating = countRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getCountRating() {
        return countRating;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Long getRssFeedId() {
        return rssFeedId;
    }

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setRssFeedId(Long rssFeedId) {
        this.rssFeedId = rssFeedId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
