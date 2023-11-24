package com.query.jpa.arie.demoquery.mapper;

import com.query.jpa.arie.demoquery.dto.AddressDTO;
import com.query.jpa.arie.demoquery.dto.EmployeeDTO;
import com.query.jpa.arie.demoquery.dto.MeetingDTO;
import com.query.jpa.arie.demoquery.dto.ProjectDTO;
import com.query.jpa.arie.demoquery.entity.Address;
import com.query.jpa.arie.demoquery.entity.Employee;
import com.query.jpa.arie.demoquery.entity.Meeting;
import com.query.jpa.arie.demoquery.entity.Project;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper{

    static int count = 0;

    public static EmployeeDTO toDto (Employee employee){
        return EmployeeDTO.builder()
            .id(employee.getId())
            .name(employee.getName())
            .address(toAddressDTO(employee.getAddress()))
            .projects(toProjectDTOs(employee.getProjects()))
            .meetings(toMeetingDTOs(employee.getMeetings()))
            .build();
    }

    private static AddressDTO toAddressDTO (Address address){
        System.out.println("Calling address................"+count);
        return AddressDTO.builder()
            .city(address.getCity())
            .id(address.getId())
            .build();
    }

    private static Set<ProjectDTO> toProjectDTOs (Set<Project> projects){
        System.out.println("Calling projects................"+count);
        return projects.stream().map(EmployeeMapper::toProjectDTO).collect(Collectors.toSet());
    }

    private static ProjectDTO toProjectDTO (Project project){
        return ProjectDTO.builder()
            .projectName(project.getProjectName())
            .build();
    }

    private static Set<MeetingDTO> toMeetingDTOs (Set<Meeting> meeting){
        System.out.println("Calling meetings................"+count);
        count+=1;
        return meeting.stream().map(EmployeeMapper::toMeetingDTO).collect(Collectors.toSet());
    }

    private static MeetingDTO toMeetingDTO (Meeting meeting){
        return MeetingDTO.builder()
            .meetingName(meeting.getMeetingName())
            .build();
    }
}
