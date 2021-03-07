package org.example.API.rest;

import org.example.API.controller.NewsController;
import org.example.API.model.Content;
import org.example.API.model.Rating;
import org.example.API.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestRatingController {

    Logger logger = LoggerFactory.getLogger(RestRatingController.class);

    @Autowired
    private RatingRepository repository;

    @Autowired
    private RestContentController contentController;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/ratings")
    List<Rating> all() {
        return repository.findAll();
    }

    @PostMapping("/ratings")
    Rating newRating(@RequestBody Rating newRating) {

        // check if rating already exists, meaning same person_id and content_id
        if(repository.existsByPersonIdAndContentId(newRating.getPersonId(),newRating.getContentId())) {
            // update rating
            logger.info("updating rating for article " + newRating.getContentId() + " from user " + newRating.getPersonId());
            Rating ratingFromDB = repository.findByPersonIdAndContentId(newRating.getPersonId(),newRating.getContentId());
            replaceRating(ratingFromDB,ratingFromDB.getId());
            logger.info("updated rating");

        }
        //TODO Includes this into a service class

        // Write to db
        Rating rating = repository.save(newRating);
        // Calculate Content (sum_rating, average_rating, count_rating)
        Content modifiedContent = contentController.one(newRating.getContentId());
        // count_rating
        modifiedContent.setCountRating(modifiedContent.getCountRating() + 1);
        // sum_rating
        double sum = modifiedContent.getSumRating() + rating.getCredibility() + rating.getInformativity() + rating.getNeutrality();
        modifiedContent.setSumRating(sum);
        // average_rating, divide by 3 because we currently have 3 Rating categories
        double averageRating = modifiedContent.getSumRating() / (modifiedContent.getCountRating() * 3);
        modifiedContent.setAverageRating(averageRating);
        contentController.replaceContent(modifiedContent, modifiedContent.getId());
        return rating;

    }

    // Single item
    @GetMapping("/ratings/{id}")
    Rating one(@PathVariable Long id) throws Exception {

        return repository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id));

    }

    //Needs to get same functions as Post does
    //@PutMapping("/ratings/{id}")
    Rating replaceRating(@RequestBody Rating newRating, @PathVariable Long id) {

        return repository.findById(id)
                .map(rating -> {
                    rating.setCredibility(newRating.getCredibility());
                    rating.setInformativity(newRating.getInformativity());
                    rating.setNeutrality(newRating.getNeutrality());
                    return repository.save(rating);
                })
                .orElseGet(() -> {
                    newRating.setId(id);
                    return repository.save(newRating);
                });
    }

    @DeleteMapping("/ratings/{id}")
    void deleteRating(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
