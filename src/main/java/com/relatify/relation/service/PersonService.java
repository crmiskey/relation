package com.relatify.relation.service;

import com.relatify.relation.dto.PersonSearchRequest;
import com.relatify.relation.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<Person> searchPersons(PersonSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDirection()),
                        request.getSortBy())
        );

        Query query = new Query().with(pageable);
        List<Criteria> criteriaList = new ArrayList<>();

        if (request.getName() != null && !request.getName().isEmpty()) {
            criteriaList.add(Criteria.where("name").regex(request.getName(), "i"));
        }

        if (request.getGender() != null && !request.getGender().isEmpty()) {
            criteriaList.add(Criteria.where("gender").is(request.getGender()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList));
        }

        List<Person> persons = mongoTemplate.find(query, Person.class);
        long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Person.class);

        return new PageImpl<>(persons, pageable, count);
    }
}
