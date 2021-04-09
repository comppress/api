package org.example.API.repository;

import org.example.API.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {

    public boolean existsByLink(String link);

    public List<Content> findTop25ByCategory(String category);

    @Query(value = "SELECT * FROM content WHERE category= :category ORDER BY id DESC LIMIT :listLenght", nativeQuery = true)
    public List<Content> selectContentOrderById(@Param("category") String category, @Param("listLenght") Integer listLenght);

    @Query(value = "SELECT * FROM content WHERE category= :category ORDER BY creation_date DESC LIMIT :listLenght", nativeQuery = true)
    public List<Content> selectContentOrderByCreationDate(@Param("category") String category, @Param("listLenght") Integer listLenght);

    @Query(value = "SELECT * FROM content WHERE category= :category ORDER BY id DESC", nativeQuery = true)
    public List<Content> selectContentOrderById(@Param("category") String category);

    public List<Content> findByCategory(String category);

    @Query(value = "SELECT * FROM content LIMIT :listLenght ;", nativeQuery = true)
    public List<Content> findAllLimit(@Param("listLenght") Integer listLenght);

    @Query(value= "SELECT * FROM content WHERE creation_date >= NOW() - interval 1 day;" , nativeQuery = true)
    public List<Content> selectContentDay();

    @Query(value= "SELECT * FROM content WHERE creation_date >= NOW() - interval 1 week;" , nativeQuery = true)
    public List<Content> selectContentWeek();

    @Query(value= "SELECT * FROM content WHERE creation_date >= NOW() - interval 1 month;" , nativeQuery = true)
    public List<Content> selectContentMonth();

    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating = 0 AND creation_date >= NOW() - interval 1 day ORDER BY creation_date DESC LIMIT :listLenght ;" , nativeQuery = true)
    public List<Content> allDayNotRated(@Param("category") String category,@Param("listLenght") Integer listLenght);
    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating >= 1 AND creation_date >= NOW() - interval 1 day ORDER BY average_rating DESC LIMIT :listLenght ;" , nativeQuery = true)
    public List<Content> allDayRated(@Param("category") String category,@Param("listLenght") Integer listLenght);

    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating = 0 AND creation_date >= NOW() - interval 1 week ORDER BY creation_date DESC LIMIT :listLenght ;" , nativeQuery = true)
    public List<Content> allWeekNotRated(@Param("category") String category,@Param("listLenght") Integer listLenght);
    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating >= 1 AND creation_date >= NOW() - interval 1 week ORDER BY average_rating DESC LIMIT :listLenght ;" , nativeQuery = true)
    public List<Content> allWeekRated(@Param("category") String category,@Param("listLenght") Integer listLenght);

    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating = 0 AND creation_date >= NOW() - interval 1 month ORDER BY creation_date DESC LIMIT :listLenght ;" , nativeQuery = true)
    public List<Content> allMonthNotRated(@Param("category") String category,@Param("listLenght") Integer listLenght);
    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating >= 1 AND creation_date >= NOW() - interval 1 month ORDER BY average_rating DESC LIMIT :listLenght ;" , nativeQuery = true)
    public List<Content> allMonthRated(@Param("category") String category,@Param("listLenght") Integer listLenght);

    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating = 0" , nativeQuery = true)
    public List<Content> selectCountRatingEqualsAndAndCategory(@Param("category") String category);

    @Query(value="SELECT * FROM content WHERE category = :category AND count_rating >= 1" , nativeQuery = true)
    public List<Content> selectCountRatingGreaterThanAndCategory(@Param("category") String category);

}