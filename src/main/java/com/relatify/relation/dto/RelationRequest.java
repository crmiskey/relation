package com.relatify.relation.dto;

import lombok.Data;

@Data
public class RelationRequest {
    private String from;
    private String to;
    private String type;
}
