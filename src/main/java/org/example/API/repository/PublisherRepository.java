package org.example.API.repository;

import org.example.API.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {

    public Publisher findByNewsAgency(String newsAgency);

}
