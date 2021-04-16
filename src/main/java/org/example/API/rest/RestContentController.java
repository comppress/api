package org.example.API.rest;

import org.example.API.model.Content;
import org.example.API.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestContentController {

    @Autowired
    private ContentRepository repository;

    @GetMapping("/contents")
    List<Content> all(@RequestParam("category") String category,@RequestParam("rating") String rating, @RequestParam("count") Integer count, @RequestParam("timeFrame") String timeFrame) {

        if(!timeFrame.isEmpty()&&!count.equals(null)&&!category.isEmpty()){
            if(!(timeFrame.equals("day") || timeFrame.equals("week") || timeFrame.equals("month"))){
               System.out.print("Wrong Input");
               return null;
            }

            if(timeFrame.equals("day")){
                if(rating.equals("true")){
                    return repository.allDayRated(category,count);
                }else {
                    List<Content> contentList= repository.allDayNotRated(category,count);
                    return contentList;
                }

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
            return repository.selectCountRatingGreaterThanAndCategory(category);
        }

        // Sorted by rating
        if(!category.isEmpty()&&rating.equals("false")){
            // Count Rating 0, to make sure
            return repository.selectCountRatingEqualsAndAndCategory(category);
        }

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
    /*
    @PostMapping("/contents")
    Content newContents(@RequestBody Content newContent) {
        return repository.save(newContent);
    }
    */
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
