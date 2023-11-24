package com.query.jpa.arie.demoquery.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class EmployeeRequest {
    private Set<String> name;
    private Set<String> meetingNames;
}
