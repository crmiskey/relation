package com.relatify.relation.repository;

import com.relatify.relation.model.Relation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RelationRepository extends MongoRepository<Relation, String> {

    boolean existsByFromAndToAndType(String from, String to, String type);
}
