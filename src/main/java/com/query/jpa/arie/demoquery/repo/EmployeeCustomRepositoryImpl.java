package com.query.jpa.arie.demoquery.repo;

import com.query.jpa.arie.demoquery.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    @Autowired
    private EntityManager entityManager;
    @Override
    public List<Object[]> findCustomEmployeeByMeetingName(String meetingName) {
        String sqlQuery = """
                SELECT
                    e.name as employeeName,
                    m.meeting_name as meetingName
                FROM Employee e
                JOIN EMPLOYEE_MEETING em ON e.id = em.employee_id
                JOIN Meeting m ON em.meeting_id = m.id
                WHERE LOWER(m.meeting_name) = LOWER(:meetingName)
                """;

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("meetingName", meetingName);

        return query.getResultList();
    }

    @Override
    public List<Employee> findOldSkullEmployeeByMeetingName(String meetingName) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("SELECT distinct e.* ");
        sqlQuery.append(" FROM employee e ");
        sqlQuery.append(" JOIN employee_meeting em ON e.id = em.employee_id ");
        sqlQuery.append(" JOIN meeting m ON em.meeting_id = m.id ");

        if (meetingName != null) {
            sqlQuery.append(" WHERE LOWER(m.meeting_name) = LOWER(:meetingName)");
            params.put("meetingName",  meetingName );
        }

        Query query = entityManager.createNativeQuery(sqlQuery.toString(), Employee.class);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.getResultList();
    }
}
