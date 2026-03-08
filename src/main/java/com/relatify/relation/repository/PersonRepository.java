package com.relatify.relation.repository;

import com.relatify.relation.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    // Custom query example: find all people from a specific native place
    List<Person> findByNativePlace(String nativePlace);
}
