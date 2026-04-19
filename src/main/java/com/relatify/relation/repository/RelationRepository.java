package com.relatify.relation.repository;

import com.relatify.relation.model.Relation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RelationRepository extends MongoRepository<Relation, String> {

    boolean existsByFromAndToAndType(String from, String to, String type);
    List<Relation> findByFrom(String from);

    List<Relation> findByTo(String to);

    List<Relation> findByFromOrTo(String from, String to);
}
