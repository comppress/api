package org.example.API.service;

import org.example.API.model.Publisher;
import org.example.API.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl implements PublisherService{

    @Autowired
    PublisherRepository publisherRepository;

    @Override
    public void deleteAllEntries() {
        publisherRepository.deleteAll();
    }

    @Override
    public Publisher getPublisherId(String newsAgency) {
        Publisher publisher = publisherRepository.findByNewsAgency(newsAgency);
        return publisher;
    }

}
