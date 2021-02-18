package org.example.API.repository;

import org.example.API.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    List<Rating> findRatingsByContentId(Long ContentId);

    public boolean existsByContentId(Long contentId);

    public boolean existsByPersonIdAndContentId(Long personId, Long contentId);

    Rating findByPersonIdAndContentId(Long personId, Long contentId);

    public Long countByContentId(Long contentId);

}
