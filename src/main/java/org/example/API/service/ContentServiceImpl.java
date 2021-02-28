package org.example.API.service;

import org.example.API.Information.Information;
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
            Information.CONTENT_SUBMITTED_TO_DB++;
            contentRepository.save(content);
            if(count % 100 == 0) {
                contentRepository.flush();
                logger.debug("Flushed "+count+" changes");
            }
        }
        contentRepository.flush();
        logger.debug("Flushed "+count+" changes");
    }

    @Override
    public boolean contentExists(String url) {
        return contentRepository.existsByLink(url);
    }

    @Override
    public Integer getNumberContent() {
        return contentRepository.findAll().size();
    }

    @Override
    public List<Content> getContentWithCategory(String category) {
            return contentRepository.findTop25ByCategory(category);
    }

    @Override
    public List<Content> getContentWithCategoryAndListLength(String category, Integer listLength) {
        return contentRepository.nativeQueryCategory(category,listLength);
    }

    @Override
    public List<Content> getContentWithCategoryAndListLengthSortedByDate(String category, Integer listLenght) {
        return contentRepository.nativeQueryCategory3(category, listLenght);
    }

    @Override
    public List<Content> getContentWitCategoryAndBestRating(String category, Integer listLength, Integer threshold) {

        return null;
    }

    @Override
    public List<Long> findAllContentFromCategroy(String category) {
        List<Content> contentList = contentRepository.nativeQueryCategory2(category);
        List<Long> idList = new ArrayList<Long>();
        for(Content content: contentList){
            if(ratingRepository.existsByContentId(content.getId())) {
                idList.add(content.getId());
            }
        }
        return idList;
    }
}
