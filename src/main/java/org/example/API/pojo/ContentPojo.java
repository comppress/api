package org.example.API.pojo;

import org.example.API.model.Content;

public class ContentPojo implements Comparable{

    private Content content;

    private Float rating;

    public ContentPojo(Content content) {
        this.content = content;
    }

    private String source;

    public void setContent(Content content) {
        this.content = content;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Content getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }

    @Override
    public int compareTo(Object o) {
        ContentPojo other = (ContentPojo) o;
        return other.rating.compareTo(this.rating);
    }
}
