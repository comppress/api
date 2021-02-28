package org.example.API.rest;

import org.example.API.component.CategoryOrder;
import org.example.API.model.Content;
import org.example.API.model.Feed;
import org.example.API.model.Rating;
import org.example.API.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FeedController {

    @Autowired
    private RestContentController contentController;

    @Autowired
    private CategoryOrder categoryOrder;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/feeds")
    List<Feed> all(@RequestParam("timeFrame") String timeFrame) throws Exception {

        Integer count = 50;

        if (timeFrame.equals("day")) {

            ArrayList<Feed> listFeed = new ArrayList<>();

            for (String category : categoryOrder.getListCategories()) {

                List<Content> unratedContent = contentController.all(category, "false", count, timeFrame);
                List<Content> ratedContent = contentController.all(category, "true", count, timeFrame);
                int size = unratedContent.size();

                // add both lists together
                unratedContent.addAll(ratedContent);
                Feed feed = new Feed(category, unratedContent);
                feed.setListContent(unratedContent);
                feed.setPositionRatedNewsStart(size);
                listFeed.add(feed);
            }

            return listFeed;

        }else{

            ArrayList<Feed> listFeed = new ArrayList<>();

            for (String category : categoryOrder.getListCategories()) {
                List<Content> ratedContent = contentController.all(category, "true", count, timeFrame);
                Feed feed = new Feed(category, ratedContent);
                // Set Position Rated News Start to 0, could be also something else
                feed.setPositionRatedNewsStart(0);
                listFeed.add(feed);
            }

            return listFeed;

        }

    }
}
