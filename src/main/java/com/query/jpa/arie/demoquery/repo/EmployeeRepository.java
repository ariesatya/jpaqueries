package com.query.jpa.arie.demoquery.repo;

import com.query.jpa.arie.demoquery.dto.EmployeeMeetingProjection;
import com.query.jpa.arie.demoquery.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

//    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "employee-graph")
//    @EntityGraph(attributePaths = {"address"}, type = EntityGraph.EntityGraphType.FETCH)
    @EntityGraph(attributePaths = { "address", "projects"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Employee> findAll();

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "employee-graph")
    Page<Employee> findAll(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "employee-graph")
    List<Employee> findAll(Specification specification);

    Optional<Employee> findOneByNameContainingIgnoreCase(String name);

    List<Employee> findByNameLike(String name);

    List<Employee> findByNameLikeAndProjects_ProjectName(String name, String project);

//    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "employee-graph")
    @Query("SELECT e FROM Employee e JOIN e.meetings m WHERE LOWER(m.meetingName) = LOWER(:meetingName)")
    List<Employee> findEmployeesByMeetingName(@Param("meetingName") String meetingName);

//        @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "employee-graph")
    @Query(value = """
        SELECT
            e.*
        FROM Employee e
        JOIN EMPLOYEE_MEETING em ON e.id = em.employee_id
        JOIN Meeting m ON em.meeting_id = m.id
        WHERE LOWER(m.meeting_name) = LOWER(:meetingName)
        """,
        nativeQuery = true)
    List<Employee> findEmployeesByMeetingNameNative(@Param("meetingName") String meetingName);

    @Query(value = """
        SELECT
            e.name as employeeName,
            m.meeting_name as meetingName
        FROM Employee e
        JOIN EMPLOYEE_MEETING em ON e.id = em.employee_id
        JOIN Meeting m ON em.meeting_id = m.id
        WHERE LOWER(m.meeting_name) = LOWER(:meetingName)
        """,
        nativeQuery = true)
    List<EmployeeMeetingProjection> findEmployeeAndMeetingNamesByMeetingName(@Param("meetingName") String meetingName);
}
