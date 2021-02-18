package org.example.API.controller;

import org.example.API.component.CategoryOrder;
import org.example.API.model.*;
import org.example.API.pojo.ContentPojo;
import org.example.API.repository.ContentRepository;
import org.example.API.repository.PublisherRepository;
import org.example.API.repository.RatingRepository;
import org.example.API.repository.RssFeedRepository;
import org.example.API.service.ContentService;
import org.example.API.service.RssFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class NewsController {

    Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    ContentService contentService;

    @Autowired
    RssFeedRepository rssFeedRepository;

    @Autowired
    RssFeedService rssFeedService;

    @Autowired
    CategoryOrder categoryOrder;

    @GetMapping("/contents")
    List<Content> allContents() {
        return contentRepository.findAll();
    }
    @GetMapping("/rssFeeds")
    List<RssFeed> allRssFeeds() {
        return rssFeedRepository.findAll();
    }
    @GetMapping("/publishers")
    List<Publisher> allPublisher() {
        return publisherRepository.findAll();
    }

    // TODO View with the new news, other one with raited news
    /**
     * Rated
     *
     * @return
     */

    @GetMapping("/ratedNews")
    List<Data> getRatedData(@RequestParam(defaultValue = "50") int listLength) throws Exception {

        ArrayList<Data> dataList = new ArrayList<Data>();
        List<String> categories = categoryOrder.getListCategories();

        if(categories.isEmpty()){
            logger.error("feed csv is not parsed correctly, using Order of feeds from db table");
            categories = rssFeedService.getAllCategories();
        }

        for(String category: categories){

            // get rated Content for this Category
            List<Long> listRatedId = contentRepository.selectDistinctRatedContentWithCategory(category);
            HashMap<Long, Float> hashMapIdAverageRating = calculateAverageRating(listRatedId);
            HashMap<Long,Long> hashMapIdNumberRatings = getHashMapIdNumberRatings();

            List<Content> contentList = new ArrayList<Content>();

            for(Long contentId:hashMapIdAverageRating.keySet()){
                contentList.add(contentRepository.findById(contentId).get());
            }

            Data data = new Data();
            // Convert to ContentPojo which implements the runnable interface
            // Todo check complexity
            List<ContentPojo> contentPojoList = convertContentToPojo(contentList,hashMapIdAverageRating, hashMapIdNumberRatings);
            // Sort the list so the best rating is top, max 5
            Collections.sort(contentPojoList);

            data.setCategory(category);
            data.setListContent(contentPojoList);
            dataList.add(data);

        }

        return dataList;
    }

    /**
     * Return a List of Data Objects (consisting out of Lists of Content of different Categories).
     * Every List consists out of articles from the Day (not rated) and rated articles of the Day.
     *
     * The returned list of one category should look like this for example:
     * [Article 10 min, Article 2 min, Article Rating 4.7, Article Rating 4.3]
     *
     * An Article will appear under rated if he receives on rating
     *
     * @param listLength
     * @return
     * @throws Exception
     */
    @GetMapping("/day")
    List<Data> getRatedDataDay(@RequestParam(defaultValue = "50") int listLength) throws Exception {

        // get Rated news of the Day
        List<Data> dataList = getRatedData(listLength,"day");

        int oneListLength = listLength / 2;

        for(Data data:dataList){

            // News of the day that are unrated
            List<Content> unratedContentList = contentRepository.selectDistinctNotRatedContent(data.getCategory(), oneListLength);

            List<ContentPojo> unratedContentPojoList = convertContentToPojo2(unratedContentList);
            int sizeUnratedContentPojoList = unratedContentPojoList.size();
            data.setPositionRatedNewsStart(sizeUnratedContentPojoList);

            List<ContentPojo> ratedContentPojoList = data.getListContent();

            unratedContentPojoList.addAll(ratedContentPojoList);

            data.setListContent(unratedContentPojoList);
        }

        return dataList;
    }

    /**
     * Returns a List of Data Objects (consisting out of Lists of Content of different Categories) matching the filter,
     * passed via the arguments listLength and interval. listLength defines the length of each List of articles for each category.
     * Interval can be either 'day','week' or 'month' and sets the time interval for the articles returned.
     *
     * @param listLength
     * @param interval
     * @return
     */
    @GetMapping("/articles")
    List<Data> getRatedData(@RequestParam(defaultValue = "50") int listLength, @RequestParam("interval") String interval) throws Exception {

        long start = System.nanoTime();

        // Check if Argument for listLength is valid
        if(listLength <= 0){
            // TODO return HTTP Error Code or throw Exception
            return null;
        }

        // Check if Argument for interval is valid
        if(!(interval.equals("day") || interval.equals("week") || interval.equals("month"))){
            // TODO return HTTP Error Code or throw Exception
            return null;
        }

        // Get Categories in correct Order
        List<String> categories = categoryOrder.getListCategories();

        // Get Articles for every Category with calculated Ratings in the correct Order
        List<Data> dataList = calculateRatings(interval, categories, listLength);

        long finish = System.nanoTime();
        long timeElapsed = finish - start;

        System.out.println(interval + " Nanoseconds " + timeElapsed);
        double seconds = (double)timeElapsed / 1_000_000_000.0;
        System.out.println(interval + " Seconds " + seconds);

        return dataList;
    }

    private ArrayList<Data> calculateRatings(String interval, List<String> categories, int listLength) {

        ArrayList<Data> dataList = new ArrayList<Data>();

        for(String category: categories){

            // Get Articles with calculated rating in the correct order
            List<ContentPojo> contentPojoList = calculateRatingsAndSortContent(interval, category, listLength);

            // Add Wrapper Object Data (Category Name, Content List) to dataList
            dataList.add(new Data(category,contentPojoList));
        }

        return dataList;
    }

    private List<ContentPojo> calculateRatingsAndSortContent(String interval, String category, int listLength) {

        // Select Content that is rated
        List<Long> listRatedId = new ArrayList<Long>();

        if(interval.equals("day")){
            listRatedId = contentRepository.selectDistinctRatedContentWithCategoryIntervalDay(category);
        }
        else if(interval.equals("week")){
            listRatedId = contentRepository.selectDistinctRatedContentWithCategoryIntervalWeek(category);
        }
        else if(interval.equals("month")){
            listRatedId = contentRepository.selectDistinctRatedContentWithCategoryIntervalMonth(category) ;
        }

        HashMap<Long, Float> hashMapIdAverageRating = calculateAverageRating(listRatedId);

        HashMap<Long,Long> hashMapIdNumberRatings = getHashMapIdNumberRatings();

        List<Content> contentList = new ArrayList<Content>();
        for(Long contentId:hashMapIdAverageRating.keySet()){
            contentList.add(contentRepository.findById(contentId).get());
        }

        // Convert to ContentPojo which implements the runnable interface
        List<ContentPojo> contentPojoList = convertContentToPojo(contentList,hashMapIdAverageRating, hashMapIdNumberRatings);

        // Sort the list so the best rating is top, max 5
        Collections.sort(contentPojoList);

        // Only use listLength size of list
        if(contentPojoList.size() > listLength) {
            contentPojoList.subList(0, listLength - 1);
        }

        return contentPojoList;
    }

    /**
     * Gets all rated content, and checks how many ratings each content has
     * Then stores the id and the number of ratings in the hashmap
     *
     * @return hashMapIdNumberRatings
     */
    private HashMap<Long,Long> getHashMapIdNumberRatings(){

        HashMap<Long,Long> hashMapIdNumberRatings = new HashMap<Long,Long>();
        // Get all rated news
        List<Long> arrayListRatedContent = contentRepository.selectDistinctRatedContent();

        // Get for every rated news the number of ratings
        for(Long id:arrayListRatedContent){
            Long countRatings = ratingRepository.countByContentId(id);
            hashMapIdNumberRatings.put(id,countRatings);
        }

        return hashMapIdNumberRatings;
    }

    private HashMap<Long, Float> calculateAverageRating(List<Long> listRatedId) {

        HashMap<Long, Float> hashMapIdAverageRating = new HashMap<Long, Float>();

        for (Long contentId : listRatedId) {

            List<Rating> listRatings = ratingRepository.findRatingsByContentId(contentId);

            // People can only submit a rating, rating all 3 top categories
            // so we can expect the values to be not null and not 0
            int count = 0;
            float average = 0;

            for (Rating rating : listRatings) {

                count += rating.getCredibility();
                count += rating.getInformativity();
                count += rating.getNeutrality();

            }

            average = (float) count / ((float) listRatings.size() * 3);
            hashMapIdAverageRating.put(contentId, average);
        }
        return hashMapIdAverageRating;
    }


    // day week month
    @GetMapping("/ratedNewsInterval")
    List<Data> getRatedDataMonth(@RequestParam(defaultValue = "50") int listLength, @RequestParam("interval") String interval) throws Exception {

        ArrayList<Data> dataList = new ArrayList<Data>();
        List<String> categories = categoryOrder.getListCategories();

        if(categories.isEmpty()){
            logger.error("feed csv is not parsed correctly, using Order of feeds from db table");
            categories = rssFeedService.getAllCategories();
        }

        if(interval.equals("day")){
            interval = "interval 1 day";
        }
        if(interval.equals("week")){
            interval = "interval 1 week";
        }
        if(interval.equals("month")){
            interval = "interval 1 month";
        }

        for(String category: categories){

            // get rated Content for this Category
            // Multi Threading?
            List<Long> listRatedId = contentRepository.selectDistinctRatedContentWithCategoryInterval(category);

            HashMap<Long, Float> hashMapIdAverageRating = calculateAverageRating(listRatedId);
            HashMap<Long,Long> hashMapIdNumberRatings = getHashMapIdNumberRatings();

            List<Content> contentList = new ArrayList<Content>();

            for(Long contentId:hashMapIdAverageRating.keySet()){
                contentList.add(contentRepository.findById(contentId).get());
            }

            Data data = new Data();
            // Convert to ContentPojo which implements the runnable interface
            // Todo check complexity
            List<ContentPojo> contentPojoList = convertContentToPojo(contentList,hashMapIdAverageRating,hashMapIdNumberRatings);
            // Sort the list so the best rating is top, max 5
            Collections.sort(contentPojoList);

            data.setCategory(category);
            data.setListContent(contentPojoList);
            dataList.add(data);

        }

        return dataList;
    }

    /**
     * Endpoint for landing Page
     * @return
     */
    @GetMapping("/data")
    List<Data> allData(@RequestParam(defaultValue = "50") int listLength) throws Exception {

        List<String> categories = categoryOrder.getListCategories();

        if(categories.isEmpty()){
            logger.error("feed csv is not parsed correctly, using Order of feeds from db table");
            categories = rssFeedService.getAllCategories();
        }


        ArrayList<Data> listData = new ArrayList<Data>();

        for(String category: categories){

               Data data = new Data();
               List<Content> content = contentService.getContentWithCategoryAndListLength(category, listLength);
               List<ContentPojo> contentPojoList = convertContentToPojo(content);
               data.setCategory(category);
               data.setListContent(contentPojoList);
               listData.add(data);

        }

        return listData;
    }



    @RequestMapping("/latestNews")
    public List<Data> getLatestNews(@RequestParam(defaultValue = "50") int listLength) throws Exception {

        ArrayList<Data> listData = new ArrayList<Data>();

        List<String> categories = categoryOrder.getListCategories();

        if(categories.isEmpty()){
            logger.error("feed csv is not parsed correctly, using Order of feeds from db table");
            categories = rssFeedService.getAllCategories();
        }

        for(String category: categories){

            Data data = new Data();
            List<Content> content = contentService.getContentWithCategoryAndListLengthSortedByDate(category, listLength);
            List<ContentPojo> contentPojoList = convertContentToPojo(content);
            data.setCategory(category);
            data.setListContent(contentPojoList);
            listData.add(data);

        }
        return listData;
    }

    private List<ContentPojo> convertContentToPojo(List<Content> contentList) {

        ArrayList<ContentPojo> arrayListContentPojo = new ArrayList<ContentPojo>();

        for(Content content:contentList){

            ContentPojo contentPojo = new ContentPojo(content);

            String source = getSource(content.getRssFeedId());

            contentPojo.setSource(source);

            arrayListContentPojo.add(contentPojo);
        }

        return arrayListContentPojo;
    }

    private List<ContentPojo> convertContentToPojo(List<Content> contentList, HashMap<Long,Float> hashMap, HashMap<Long,Long> hashMapIdNumberRatings) {

        ArrayList<ContentPojo> arrayListContentPojo = new ArrayList<ContentPojo>();

        for(Content content:contentList){

            ContentPojo contentPojo = new ContentPojo(content);

            String source = getSource(content.getRssFeedId());

            contentPojo.setRating(hashMap.get(content.getId()));
            contentPojo.setSource(source);
            // What is the default value if there is no Rating?
            contentPojo.setCountRatings(hashMapIdNumberRatings.get(content.getId()));

            arrayListContentPojo.add(contentPojo);
        }

        return arrayListContentPojo;
    }

    private List<ContentPojo> convertContentToPojo2(List<Content> contentList) {

        ArrayList<ContentPojo> arrayListContentPojo = new ArrayList<ContentPojo>();

        for(Content content:contentList){

            ContentPojo contentPojo = new ContentPojo(content);

            String source = getSource(content.getRssFeedId());

            // Set Rating?

            contentPojo.setSource(source);

            arrayListContentPojo.add(contentPojo);
        }

        return arrayListContentPojo;
    }


    private String getSource(Long rssFeedId) {

        Optional<RssFeed> rssFeedOptional = rssFeedRepository.findById(rssFeedId);
        RssFeed rssFeed = rssFeedOptional.get();

        Optional<Publisher> publisherOptional= publisherRepository.findById((long)rssFeed.getPublisherId());
        Publisher publisher = publisherOptional.get();

        return publisher.getNewsAgency();
    }
}
