package com.relatify.relation.service;

import com.relatify.relation.dto.PersonNode;
import com.relatify.relation.model.Person;
import com.relatify.relation.model.Relation;
import com.relatify.relation.repository.PersonRepository;
import com.relatify.relation.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RelationshipTreeService {

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private RelationRepository relationRepo;

    public PersonNode buildTree(String personId, String direction, int maxDepth) {
        Set<String> visited = new HashSet<>();
        return buildRecursive(personId, direction, 0, maxDepth, visited);
    }

    private PersonNode buildRecursive(String personId,
                                      String direction,
                                      int currentDepth,
                                      int maxDepth,
                                      Set<String> visited) {

        // stop conditions
        if (personId == null || visited.contains(personId) || currentDepth > maxDepth) {
            return null;
        }

        visited.add(personId);

        Person person = personRepo.findById(personId).orElse(null);
        if (person == null) return null;

        PersonNode node = new PersonNode(person, currentDepth);

        List<Relation> relations;

        if ("ancestors".equalsIgnoreCase(direction)) {
            // child → parent
            relations = relationRepo.findByFrom(personId);
        } else {
            // parent → children
            relations = relationRepo.findByTo(personId);
        }

        for (Relation rel : relations) {

            // Only process parent relationships (safe guard)
            if (!"parent".equalsIgnoreCase(rel.getType())) continue;

            String nextId = "ancestors".equalsIgnoreCase(direction)
                    ? rel.getTo()   // parent
                    : rel.getFrom(); // child

            PersonNode childNode = buildRecursive(
                    nextId,
                    direction,
                    currentDepth + 1,
                    maxDepth,
                    visited
            );

            if (childNode != null) {
                node.getChildren().add(childNode);
            }
        }

        return node;
    }
}
