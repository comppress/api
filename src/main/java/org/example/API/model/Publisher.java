package org.example.API.model;

import javax.persistence.*;

@Entity
public class Publisher {

    public static final String TABLE_NAME= "ARTICLES";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name="news_agency")
    private String newsAgency;
    @Column(name="link_homepage")
    private String linkHomepage;

    public Long getId() {
        return id;
    }

    public String getNewsAgency() {
        return newsAgency;
    }

    public String getLinkHomepage() {
        return linkHomepage;
    }

    public void setNewsAgency(String newsAgency) {
        this.newsAgency = newsAgency;
    }

    public void setLinkHomepage(String linkHomepage) {
        this.linkHomepage = linkHomepage;
    }
}
