package org.example.API.model;

import javax.persistence.*;

@Entity
public class Person {

    public Person(){}

    public Person(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String name;
    private String password;
    private String session;
    @Column(name="creation_date", insertable=false)
    private String creationDate;
    @Column(name="modification_date")
    private String modification_date;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setModification_date(String modification_date) {
        this.modification_date = modification_date;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSession() {
        return session;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getModification_date() {
        return modification_date;
    }
}
