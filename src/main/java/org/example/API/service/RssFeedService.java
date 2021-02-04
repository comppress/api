package org.example.API.service;

import org.example.API.model.RssFeed;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface RssFeedService {

    public void deleteAllEntries();

    public Long getId(String url);

    public ArrayList<URL> getAllUrls();

    public HashMap<String,String> getAllUrlsWithCategory();

    public Integer getNumberRssFeeds();

    public void writeToDB(List<RssFeed> rssFeedList);

    public boolean rssFeedExists(String linkRssFeed);

    public List<String> getAllCategories();

}
