package com.query.jpa.arie.demoquery.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingDTO {
    private Long id;
    private String meetingName;
}
