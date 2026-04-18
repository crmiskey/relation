package com.relatify.relation.dto;

import com.relatify.relation.model.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonNode {

    private String id;
    private String name;
    private String gender;
    private String birthYear;
    private int depth;

    private List<PersonNode> children = new ArrayList<>();
    private List<PersonNode> spouses = new ArrayList<>();

    public PersonNode(Person person, int depth) {
        this.id = person.getId();
        this.name = person.getName();
        this.gender = person.getGender();
        this.birthYear = person.getBirthYear();
        this.depth = depth;
    }
}
