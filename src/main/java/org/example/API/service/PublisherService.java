package org.example.API.service;

import org.example.API.model.Publisher;

public interface PublisherService {

    public void deleteAllEntries();

    public Publisher getPublisherId(String newsAgency);

}
