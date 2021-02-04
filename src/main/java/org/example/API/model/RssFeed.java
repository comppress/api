package org.example.API.model;

import javax.persistence.*;

@Entity
@Table(name = RssFeed.TABLE_NAME)
public class RssFeed {

    public static final String TABLE_NAME= "rss_feed";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="category")
    private String category;
    @Column(name="link_rss_feed")
    private String linkRssFeed;
    @Column(name="creation_date", insertable=false)
    private String creationDate;
    @Column(name="modification_date")
    private String modificationDate;
    @Column(name="publisher_id")
    private Long publisherId;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLinkRssFeed(String linkRssFeed) {
        this.linkRssFeed = linkRssFeed;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getLinkRssFeed() {
        return linkRssFeed;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public Long getPublisherId() {
        return publisherId;
    }
}
