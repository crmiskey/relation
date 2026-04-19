package com.relatify.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationWithNames {

    private String id;
    private String from;
    private String fromName;
    private String to;
    private String toName;
    private String type;
}
