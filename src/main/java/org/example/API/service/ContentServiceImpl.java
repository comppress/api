package org.example.API.service;

import org.example.API.model.Content;
import org.example.API.repository.ContentRepository;
import org.example.API.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ContentRepository contentRepository;

    public void deleteAllEntries(){
        contentRepository.deleteAll();
    }

    @Override
    public void writeToDB(List<Content> list) {

        int count = 0;

        for(Content content:list) {
            if(contentRepository.existsByLink(content.getLink())){
                continue;
            }
            count++;
            contentRepository.save(content);
            if(count % 100 == 0) {
                contentRepository.flush();
                logger.debug("Flushed "+count+" changes");
            }
        }
        contentRepository.flush();
        logger.debug("Flushed "+count+" changes");
    }
}
