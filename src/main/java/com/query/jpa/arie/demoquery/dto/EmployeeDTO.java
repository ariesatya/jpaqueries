package com.query.jpa.arie.demoquery.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class EmployeeDTO {
    private Long id;
    private String name;
    private Set<ProjectDTO> projects;
    private Set<MeetingDTO> meetings;
    private AddressDTO address;
}
