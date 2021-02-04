package org.example.API.service;

import org.example.API.model.RssFeed;
import org.example.API.repository.RssFeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RssFeedServiceImpl implements RssFeedService{

    Logger logger = LoggerFactory.getLogger(RssFeedServiceImpl.class);

    @Autowired
    RssFeedRepository rssFeedRepository;

    @Override
    public void deleteAllEntries() {
        rssFeedRepository.deleteAll();
    }

    @Override
    public Long getId(String url) {
        // Guter Style, Joe Fragen?? Sollte ja eigentlich immer ein Elem sein
        RssFeed rssFeed = rssFeedRepository.findByLinkRssFeed(url);
        return rssFeed.getId();
    }

    @Override
    public ArrayList<URL> getAllUrls() {

        List<RssFeed> list = rssFeedRepository.findAll();
        ArrayList<URL> urlList = new ArrayList<URL>();
        for(RssFeed rssFeed:list){
            try {
                urlList.add(new URL(rssFeed.getLinkRssFeed()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    @Override
    public HashMap<String, String> getAllUrlsWithCategory() {

        List<RssFeed> list = rssFeedRepository.findAll();
        HashMap<String,String> mapUrlCategory = new HashMap<String, String>();
        for(RssFeed rssFeed: list){
            mapUrlCategory.put(rssFeed.getLinkRssFeed(),rssFeed.getCategory());
        }
        return mapUrlCategory;

    }

    @Override
    public Integer getNumberRssFeeds() {
        return rssFeedRepository.findAll().size();
    }

    @Override
    public void writeToDB(List<RssFeed> rssFeedList) {
        for(RssFeed rssFeed:rssFeedList) {
            rssFeedRepository.save(rssFeed);
        }
        rssFeedRepository.flush();
    }

    @Override
    public boolean rssFeedExists(String linkRssFeed) {
        return rssFeedRepository.existsByLinkRssFeed(linkRssFeed);
    }

    @Override
    public List<String> getAllCategories() {
        return rssFeedRepository.dinstinctCategories();
    }


}
