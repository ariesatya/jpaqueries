package com.query.jpa.arie.demoquery.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Request {
    private String name;
    private String projectName;
}
