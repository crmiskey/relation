package com.relatify.relation.controller;

import com.relatify.relation.dto.PersonSearchRequest;
import com.relatify.relation.model.Person;
import com.relatify.relation.repository.PersonRepository;
import com.relatify.relation.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person); // This creates the doc in MongoDB
    }

    @PostMapping("/search")
    public Page<Person> searchPersons(@RequestBody PersonSearchRequest request) {
        return personService.searchPersons(request);
    }

}
