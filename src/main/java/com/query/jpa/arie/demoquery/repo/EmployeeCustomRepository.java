package com.query.jpa.arie.demoquery.repo;

import com.query.jpa.arie.demoquery.entity.Employee;

import java.util.List;

@SuppressWarnings("unused")
public interface EmployeeCustomRepository{
    List<Object[]> findCustomEmployeeByMeetingName(String meetingName);

    List<Employee> findOldSkullEmployeeByMeetingName(String meetingName);
}
