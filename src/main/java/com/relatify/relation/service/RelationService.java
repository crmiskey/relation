package com.relatify.relation.service;

import com.relatify.relation.dto.RelationRequest;
import com.relatify.relation.dto.RelationWithNames;
import com.relatify.relation.model.Person;
import com.relatify.relation.model.Relation;
import com.relatify.relation.repository.PersonRepository;
import com.relatify.relation.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private PersonRepository personRepository;

    public Relation createRelation(RelationRequest request) {

        // 🔹 Validate request
        if (request.getFrom() == null || request.getTo() == null || request.getType() == null) {
            throw new IllegalArgumentException("from, to and type are required");
        }

        // 🔹 Validate both persons exist
        boolean fromExists = personRepository.existsById(request.getFrom());
        boolean toExists = personRepository.existsById(request.getTo());

        if (!fromExists || !toExists) {
            throw new IllegalArgumentException("Invalid person IDs: from/to not found");
        }

        // 🔹 Prevent duplicate relation
        boolean alreadyExists = relationRepository.existsByFromAndToAndType(
                request.getFrom(),
                request.getTo(),
                request.getType()
        );

        if (alreadyExists) {
            throw new IllegalStateException("Relation already exists");
        }

        // 🔹 Save relation
        Relation relation = new Relation();
        relation.setFrom(request.getFrom());
        relation.setTo(request.getTo());
        relation.setType(request.getType());

        return relationRepository.save(relation);
    }

    public List<RelationWithNames> getRelationsWithNames(String personId) {

        // Step 1: Fetch relations
        List<Relation> relations = relationRepository.findByFromOrTo(personId, personId);

        // Step 2: Collect all unique person IDs
        Set<String> personIds = new HashSet<>();
        for (Relation r : relations) {
            personIds.add(r.getFrom());
            personIds.add(r.getTo());
        }

        // Step 3: Fetch all persons in one go
        Map<String, Person> personMap = personRepository.findAllById(personIds)
                .stream()
                .collect(Collectors.toMap(Person::getId, p -> p));

        // Step 4: Build response
        List<RelationWithNames> result = new ArrayList<>();

        for (Relation r : relations) {

            Person fromPerson = personMap.get(r.getFrom());
            Person toPerson = personMap.get(r.getTo());

            result.add(new RelationWithNames(
                    r.getId(),
                    r.getFrom(),
                    fromPerson != null ? fromPerson.getName() : null,
                    r.getTo(),
                    toPerson != null ? toPerson.getName() : null,
                    r.getType()
            ));
        }

        return result;
    }

}
