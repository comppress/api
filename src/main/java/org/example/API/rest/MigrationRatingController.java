package org.example.API.rest;

import org.example.API.model.Content;
import org.example.API.model.Rating;
import org.example.API.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MigrationRatingController {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    RestContentController contentController;

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

}
