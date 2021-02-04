package org.example.API.controller;

import org.example.API.model.Person;
import org.example.API.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/createTestUser")
    Person getNewPerson(){
        Person person = new Person();
        person.setName("Max Mustermann");
        person.setPassword("Password");
        // Should return person object with auto generated id
        person = personRepository.saveAndFlush(person);
        logger.info("Created new Person in DB with id " + person.getId() );
        return person;
    }

    @GetMapping("/userReference")
    Long checkUserReference(@RequestParam String name){

        logger.info("checkReference Request send from Android App User" + name);

        if(personRepository.existsByName(name)){
            logger.info("User is already in Data Base, no new User needs to be created");
        } else{
            Person person = new Person(name,"empty");
            personRepository.saveAndFlush(person);
            logger.info("Creating new User with name " +  person.getName());
        }

        Person person = personRepository.findByName(name);

        return person.getId();
    }

}
