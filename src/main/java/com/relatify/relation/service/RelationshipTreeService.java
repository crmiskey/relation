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

        if (personId == null || visited.contains(personId) || currentDepth > maxDepth) {
            return null;
        }

        visited.add(personId);

        Person person = personRepo.findById(personId).orElse(null);
        if (person == null) return null;

        PersonNode node = new PersonNode(person, currentDepth);

        List<Relation> relations;

        if ("ancestors".equalsIgnoreCase(direction)) {
            relations = relationRepo.findByFrom(personId);
        } else {
            relations = relationRepo.findByTo(personId);
        }

        for (Relation rel : relations) {

            String type = rel.getType();

            // -------------------------
            // 1. PARENT / CHILD TREE
            // -------------------------
            if ("parent".equalsIgnoreCase(type)) {

                String nextId = "ancestors".equalsIgnoreCase(direction)
                        ? rel.getTo()
                        : rel.getFrom();

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

            // -------------------------
            // 2. SPOUSE (SIDE LINK)
            // -------------------------
            else if ("spouse".equalsIgnoreCase(type)) {

                String spouseId = rel.getFrom().equals(personId)
                        ? rel.getTo()
                        : rel.getFrom();

                // DO NOT recurse into spouse (prevents cycles)
                Person spouse = personRepo.findById(spouseId).orElse(null);

                if (spouse != null) {
                    node.getSpouses().add(new PersonNode(spouse, currentDepth));
                }
            }
        }

        return node;
    }
}
