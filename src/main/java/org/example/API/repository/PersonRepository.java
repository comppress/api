package org.example.API.repository;

import org.example.API.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {

    public boolean existsByName(String name);

    public Person findByName(String name);

}
