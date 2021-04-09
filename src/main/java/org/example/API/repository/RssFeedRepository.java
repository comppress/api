package org.example.API.repository;

import org.example.API.model.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RssFeedRepository extends JpaRepository<RssFeed,Long> {

    public RssFeed findByLinkRssFeed(String linkRssFeed);

    public boolean existsByLinkRssFeed(String linkRssFeed);

    @Query(value = "SELECT DISTINCT(category) FROM mydb.rss_feed;", nativeQuery = true)
    public List<String> distinctCategories();

}
