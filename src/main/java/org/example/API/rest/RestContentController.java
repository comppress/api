package org.example.API.rest;

import org.example.API.model.Content;
import org.example.API.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestContentController {

    @Autowired
    private ContentRepository repository;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/contents")
    List<Content> all(@RequestParam("category") String category,@RequestParam("rating") String rating, @RequestParam("count") Integer count, @RequestParam("timeFrame") String timeFrame) {

        // TODO Rating true false richtig umsetzen, heißt false das nicht gerateded Nachrichten angezeigt werden, i guess !?
        // Rated Articles haben ein count Rating greater 0, Unrated equals 0

        // Method handling all 4 Arguments
        if(!timeFrame.isEmpty()&&!count.equals(null)&&!category.isEmpty()){
            if(!(timeFrame.equals("day") || timeFrame.equals("week") || timeFrame.equals("month"))){
               System.out.print("Wrong Input");
               return null;
            }
            // Todo, good way of handling time Zones, pass a Timeframe in SQL, probably better, can search for this via Spring Data, frontend need to provide Frame
            // Must be a away to configure this, instead of doing this in such a static way
            if(timeFrame.equals("day")){
                if(rating.equals("true")){
                    return repository.allDayRated(category,count);
                }else return repository.allDayNotRated(category,count);

            }else if(timeFrame.equals("week")){
                if(rating.equals("true")){
                    return repository.allWeekRated(category,count);
                }else return repository.allWeekNotRated(category,count);
            }else if(timeFrame.equals("month")){
                if(rating.equals("true")){
                    return repository.allMonthRated(category,count);
                }else return repository.allMonthNotRated(category,count);
            }

        }

        // Sorted by rating
        if(!category.isEmpty()&&rating.equals("true")){
            return repository.nativeQueryfindByCountRatingGreaterThanAndCategory(category);
        }

        // Sorted by rating
        if(!category.isEmpty()&&rating.equals("false")){
            // Count Rating 0, to make sure
            return repository.nativeQueryfindByCountRatingEqualsAndAndCategory(category);
        }

        // TODO Generate Query String during RunTime?

        // TODO for Future, no static time intervals, instead dynamic Time Frame

        //timeframe, category, Sorted by rating, count
        if(!timeFrame.isEmpty()){
            if(timeFrame.equals("day")){
                return repository.selectContentDay();
            }
            else if(timeFrame.equals("week")){
                return repository.selectContentWeek();
            }
            else if(timeFrame.equals("month")){
                return repository.selectContentMonth();
            }else{
                // Todo Throw Error Code
                System.out.println("Invalid Argument");
            }
        }

        // count
        if(!count.equals(null)){
            return repository.findAllLimit(count);
        }

        // category
        if(!category.isEmpty()){
            return repository.findByCategory(category);
        }

        return repository.findAll();
    }

    @PostMapping("/contents")
    Content newContents(@RequestBody Content newContent) {
        return repository.save(newContent);
    }

    // Single item
    @GetMapping("/contents/{id}")
    Content one(@PathVariable Long id) throws RatingNotFoundException {

        return repository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id));

    }

    @PutMapping("/contents/{id}")
    Content replaceContent(@RequestBody Content newContent, @PathVariable Long id) {

        return repository.findById(id)
                .map(content -> {
                    content.setLink(newContent.getLink());
                    content.setTitle(newContent.getTitle());
                    content.setImageLink(newContent.getImageLink());
                    content.setSource(newContent.getSource());
                    content.setSumRating(newContent.getSumRating());
                    content.setCountRating(newContent.getCountRating());
                    content.setAverageRating(newContent.getAverageRating());
                    content.setCategory(newContent.getCategory());
                    content.setRssFeedId(newContent.getRssFeedId());
                    return repository.save(content);
                })
                .orElseGet(() -> {
                    newContent.setId(id);
                    return repository.save(newContent);
                });
    }

    @DeleteMapping("/contents/{id}")
    void deleteRating(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
