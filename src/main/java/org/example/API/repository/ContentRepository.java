package org.example.API.repository;

import org.example.API.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {

    public boolean existsByLink(String link);

    public List<Content> findTop25ByCategory(String category);

    @Query(value = "SELECT * FROM mydb.content WHERE category= :category ORDER BY id DESC LIMIT :listLenght", nativeQuery = true)
    public List<Content> nativeQueryCategory(@Param("category") String category,@Param("listLenght") Integer listLenght);

    @Query(value = "SELECT * FROM mydb.content WHERE category= :category ORDER BY creation_date DESC LIMIT :listLenght", nativeQuery = true)
    public List<Content> nativeQueryCategory3(@Param("category") String category,@Param("listLenght") Integer listLenght);

    //public List<Content> findTop20ByCategoryOrderByCreationDateCategoryDesc(String Category);

    @Query(value= "SELECT * FROM mydb.content LIMIT :listLenght ORDER BY id DESC", nativeQuery = true)
    public List<Content> getContentListLimit(@Param("listLenght") Integer listLenght);

    @Query(value= "SELECT * FROM mydb.content ORDER BY RAND() LIMIT :listLenght", nativeQuery = true)
    public List<Content> selectRandomList(@Param("listLenght") Integer listLenght);

    @Query(value = "SELECT * FROM mydb.content WHERE category= :category ORDER BY id DESC", nativeQuery = true)
    public List<Content> nativeQueryCategory2(@Param("category") String category);

    // Maybe Problem das die c reference und so
    @Query(value= "SELECT c.id FROM mydb.content c  inner join mydb.rating r on category=:categoryValue ;" , nativeQuery = true)
    public List<Long> findAllRatedContentFromCategroy(@Param("categoryValue") String category1);

    // Maybe Problem das die c reference und so
    @Query(value= "SELECT * FROM mydb.content WHERE id IN (SELECT DISTINCT content_id FROM mydb.rating ) AND category = :categoryValue ;" , nativeQuery = true)
    public List<Long> selectDistinctRatedContentWithCategory(@Param("categoryValue") String category1);

    @Query(value= "SELECT * FROM mydb.content WHERE id IN (SELECT DISTINCT content_id FROM mydb.rating ) AND category = :categoryValue AND creation_date >= NOW() - interval 1 day;" , nativeQuery = true)
    public List<Long> selectDistinctRatedContentWithCategoryIntervalDay(@Param("categoryValue") String category);

    @Query(value= "SELECT * FROM mydb.content WHERE id IN (SELECT DISTINCT content_id FROM mydb.rating ) AND category = :categoryValue AND creation_date >= NOW() - interval 1 week;" , nativeQuery = true)
    public List<Long> selectDistinctRatedContentWithCategoryIntervalWeek(@Param("categoryValue") String category);

    @Query(value= "SELECT * FROM mydb.content WHERE id IN (SELECT DISTINCT content_id FROM mydb.rating ) AND category = :categoryValue AND creation_date >= NOW() - interval 1 month;" , nativeQuery = true)
    public List<Long> selectDistinctRatedContentWithCategoryIntervalMonth(@Param("categoryValue") String category);

    @Query(value= "SELECT * FROM mydb.content WHERE id IN (SELECT DISTINCT content_id FROM mydb.rating ) AND category = :categoryValue ;" , nativeQuery = true)
    public List<Long> selectDistinctRatedContentWithCategoryInterval(@Param("categoryValue") String category);

    @Query(value= "SELECT * FROM mydb.content WHERE id NOT IN (SELECT DISTINCT content_id FROM mydb.rating ) AND category = :categoryValue AND creation_date >= NOW() - interval 1 day ORDER BY creation_date DESC LIMIT :limitNumber ;" , nativeQuery = true)
    public List<Content> selectDistinctNotRatedContent(@Param("categoryValue") String category, @Param("limitNumber") int limitNumber);

    @Query(value= "SELECT * FROM mydb.content WHERE id IN (SELECT DISTINCT content_id FROM mydb.rating )" , nativeQuery = true)
    public List<Long> selectDistinctRatedContent();

}
