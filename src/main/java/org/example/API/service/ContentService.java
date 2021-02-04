package org.example.API.service;

import org.example.API.model.Content;

import java.util.List;

public interface ContentService {

    public void deleteAllEntries();

    public void writeToDB(List<Content> list);

    public boolean contentExists(String url);

    public Integer getNumberContent();

    public List<Content> getContentWithCategory(String category);

    public List<Content> getContentWithCategoryAndListLength(String category, Integer listLength);

    public List<Content> getContentWithCategoryAndListLengthSortedByDate(String category, Integer listLenght);

    public List<Content> getContentWitCategoryAndBestRating(String category, Integer listLength, Integer threshold);

    List<Long> findAllContentFromCategroy(String category);
}
