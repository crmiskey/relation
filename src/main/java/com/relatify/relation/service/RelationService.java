package com.relatify.relation.service;

import com.relatify.relation.dto.RelationRequest;
import com.relatify.relation.model.Relation;
import com.relatify.relation.repository.PersonRepository;
import com.relatify.relation.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
