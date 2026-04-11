package com.relatify.relation.dto;

import lombok.Data;

@Data
public class PersonSearchRequest {

    private String name;
    private String gender;

    private int page = 0;
    private int size = 10;

    // optional: sorting
    private String sortBy = "name";
    private String sortDirection = "asc";
}
