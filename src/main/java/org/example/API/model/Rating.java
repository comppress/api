package org.example.API.model;

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="content_id")
    private Long contentId;
    @Column(name="person_id")
    private Long personId;
    @Column(name="creation_date", insertable=false)
    private String creationDate;
    private Integer credibility;
    private Integer informativity;
    private Integer factuality;
    @Column(name="source_transparency")
    private Integer sourceTransparency;
    private Integer neutrality;
    @Column(name="plurality_of_views")
    private Integer pluralityOfViews;
    private Integer impartiality;
    private Integer dispassion;

    public void setId(Long id) {
        this.id = id;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setCredibility(Integer credibility) {
        this.credibility = credibility;
    }

    public void setInformativity(Integer informativity) {
        this.informativity = informativity;
    }

    public void setFactuality(Integer factuality) {
        this.factuality = factuality;
    }

    public void setSourceTransparency(Integer sourceTransparency) {
        this.sourceTransparency = sourceTransparency;
    }

    public void setNeutrality(Integer neutrality) {
        this.neutrality = neutrality;
    }

    public void setPluralityOfViews(Integer pluralityOfViews) {
        this.pluralityOfViews = pluralityOfViews;
    }

    public void setImpartiality(Integer impartiality) {
        this.impartiality = impartiality;
    }

    public void setDispassion(Integer dispassion) {
        this.dispassion = dispassion;
    }

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public Long getPersonId() {
        return personId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public Integer getCredibility() {
        return credibility;
    }

    public Integer getInformativity() {
        return informativity;
    }

    public Integer getFactuality() {
        return factuality;
    }

    public Integer getSourceTransparency() {
        return sourceTransparency;
    }

    public Integer getNeutrality() {
        return neutrality;
    }

    public Integer getPluralityOfViews() {
        return pluralityOfViews;
    }

    public Integer getImpartiality() {
        return impartiality;
    }

    public Integer getDispassion() {
        return dispassion;
    }
}
