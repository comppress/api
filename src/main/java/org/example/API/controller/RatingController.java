package org.example.API.controller;

import org.example.API.model.Content;
import org.example.API.model.Person;
import org.example.API.model.Rating;
import org.example.API.pojo.RatingPojo;
import org.example.API.repository.ContentRepository;
import org.example.API.repository.PersonRepository;
import org.example.API.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class RatingController {

    Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    PersonRepository personRepository;

    @PostMapping("/postRating")
    public void createRatingTemp(@RequestBody RatingPojo ratingPojo){

        Rating rating = ratingPojo.getRating();
        // check if rating already exists, meaning same person_id and content_id
        if(ratingRepository.existsByPersonIdAndContentId(rating.getPersonId(),rating.getContentId())) {
            // update rating
            logger.info("updating rating for article " + rating.getContentId() + " from user " + rating.getPersonId());
            Rating ratingFromDB = ratingRepository.findByPersonIdAndContentId(rating.getPersonId(),rating.getContentId());
            updateRatings(ratingFromDB, rating);
            ratingRepository.save(ratingFromDB);
            logger.info("updated rating");
        } else {
            // create rating
            logger.info("create new rating for article " + rating.getContentId() + "from user " + rating.getPersonId());
            ratingRepository.saveAndFlush(rating);
            logger.info("created rating");
        }
    }

    private void updateRatings(Rating ratingFromDB, Rating rating) {
        // only update Hauptkriterien so far
        ratingFromDB.setCredibility(rating.getCredibility());
        ratingFromDB.setInformativity(rating.getInformativity());
        ratingFromDB.setNeutrality(rating.getNeutrality());
    }

    @GetMapping("/getRating")
    public RatingPojo getRating(@RequestParam Long id){
        if(id == null){
            return null;
        }


        Optional<Rating> ratingOptional = ratingRepository.findById(id);

        RatingPojo rating = new RatingPojo();
        rating.setRating(ratingOptional.get());
        return rating;
    }

    @GetMapping("/generateRandomRatings")
    public void generateRandomRatings(){

        // check if there is a person (user), create new one if not

        Long personid = -1l;
        if(personRepository.findAll().size() == 0){
            Person person = new Person();
            person.setName("Test-User");
            person.setPassword("Test-Password");
            personRepository.saveAndFlush(person);
        }else{
            List<Person> personList = personRepository.findAll();
            personid = personList.get(0).getId();
        }

        // number random ratings
        int numberRatings = 1000;

        List<Content> contentList = contentRepository.selectRandomList(numberRatings);

        for(Content content:contentList){
            Long id = content.getId();
            Long personId = personid;

            Random rand = new Random();

            int random1 = rand.nextInt(5) + 1;
            int random2 = rand.nextInt(5) + 1;
            int random3 = rand.nextInt(5) + 1;

            Rating rating = new Rating();
            rating.setCredibility(random1);
            rating.setNeutrality(random2);
            rating.setInformativity(random3);
            rating.setContentId(id);
            rating.setPersonId(personId);
            ratingRepository.save(rating);
        }
        ratingRepository.flush();
    }


}
