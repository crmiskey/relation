package com.relatify.relation.controller;

import com.relatify.relation.dto.RelationRequest;
import com.relatify.relation.model.Relation;
import com.relatify.relation.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/relations")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @PostMapping
    public Relation createRelation(@RequestBody RelationRequest request) {
        return relationService.createRelation(request);
    }
}
