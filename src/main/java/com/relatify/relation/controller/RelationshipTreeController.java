package com.relatify.relation.controller;

import com.relatify.relation.dto.PersonNode;
import com.relatify.relation.service.RelationshipTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tree")
public class RelationshipTreeController {

    @Autowired
    private RelationshipTreeService service;

    @GetMapping
    public ResponseEntity<PersonNode> getTree(
            @RequestParam String personId,
            @RequestParam String direction, // ancestors | descendants
            @RequestParam(defaultValue = "3") int depth
    ) {

        PersonNode tree = service.buildTree(personId, direction, depth);
        return ResponseEntity.ok(tree);
    }
}
