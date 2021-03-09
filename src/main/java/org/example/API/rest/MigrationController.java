package org.example.API.rest;

import org.example.API.model.Content;
import org.example.API.model.Rating;
import org.example.API.model.RssFeed;
import org.example.API.repository.ContentRepository;
import org.example.API.repository.PublisherRepository;
import org.example.API.repository.RatingRepository;
import org.example.API.repository.RssFeedRepository;
import org.example.API.service.RssFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class MigrationController {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    RestContentController contentController;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    RssFeedRepository rssFeedRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @GetMapping("migrateRatings")
    public void migrateRatings(){

        List<Rating> ratingList = ratingRepository.findAll();

        for(Rating rating: ratingList){

            Content modifiedContent = contentController.one(rating.getContentId());
            // count_rating
            modifiedContent.setCountRating(modifiedContent.getCountRating() + 1);
            // sum_rating
            double sum = modifiedContent.getSumRating() + rating.getCredibility() + rating.getInformativity() + rating.getNeutrality();
            modifiedContent.setSumRating(sum);
            // average_rating, divide by 3 because we currently have 3 Rating categories
            double averageRating = modifiedContent.getSumRating() / (modifiedContent.getCountRating() * 3);
            modifiedContent.setAverageRating(averageRating);
            contentController.replaceContent(modifiedContent, modifiedContent.getId());

            System.out.println("Update Content: " + modifiedContent.getId() + " Rating: " + rating.getId());
        }

    }

    @GetMapping("migrateContentSource")
    public void migrateContent(){

        List<Content> contentList = contentRepository.findAll();

        int countChangedContent = 0;

        for(Content content: contentList){

            if(content.getSource() == null || content.getSource().isEmpty()) {

                Long rssFeedId = content.getRssFeedId();
                Optional<RssFeed> rssFeed = rssFeedRepository.findById(rssFeedId);
                Long publisherId = rssFeed.get().getPublisherId();
                String source = publisherRepository.findById(publisherId).get().getNewsAgency();
                content.setSource(source);
                countChangedContent++;

            }

            if(countChangedContent % 100 == 0){
                System.out.println("Iterated over " +countChangedContent + "Entries");
            }

        }

        // Add all Content to database
        System.out.println("Write " + countChangedContent + "new Entries to DB" );

        contentRepository.saveAll(contentList);

        System.out.print("Changed  " + countChangedContent + " Objects and edited Source");

    }

}
