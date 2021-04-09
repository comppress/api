package org.example.API.service;

import org.example.API.model.Content;

import java.util.List;

public interface ContentService {

    public void deleteAllEntries();

    public void writeToDB(List<Content> list);

}
