package com.query.jpa.arie.demoquery.repo;

import com.query.jpa.arie.demoquery.dto.EmployeeDTO;
import com.query.jpa.arie.demoquery.dto.EmployeeMeetingProjection;
import com.query.jpa.arie.demoquery.dto.EmployeeRequest;
import com.query.jpa.arie.demoquery.dto.Request;
import com.query.jpa.arie.demoquery.entity.Address;
import com.query.jpa.arie.demoquery.entity.Employee;
import com.query.jpa.arie.demoquery.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ComponentScan(basePackages = "com.query.jpa.arie.demoquery")
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCustomRepository employeeCustomRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void jpaRepository() {
        List<Employee> employees = employeeRepository.findAll();
        assertEquals(5, employees.size());

        Employee emp = employeeRepository.findById(1L).orElse(null);
        assertEquals("John Doe", emp.getName());
    }

    @Test
    void jpaRepositoryFilter() {
        List<Employee> employeeNameLike = employeeRepository.findByNameLike("J%");
        assertEquals(3, employeeNameLike.size());

        List<Employee> employeeProjects = employeeRepository.findByNameLikeAndProjects_ProjectName("J%", "Project A");
        assertEquals(1, employeeProjects.size());

        //bahaya karena bisa lebih dr satu
//        Employee emp = employeeRepository.findOneByNameContainingIgnoreCase("John").orElse(null);
//        assertEquals("John Doe", emp.getName());
    }

    @Test
    void jpaRepositoryN1() {
        //LAZY vs EAGER
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOS = employees
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
        assertEquals(5, employeeDTOS.size());
    }

    @Test
    void jpaRepositoryPageN1() {
        //eager
        Pageable pageable = PageRequest.of(0, 10);
        var employees = employeeRepository.findAll(pageable);
        List<EmployeeDTO> employeeDTOS = employees
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
        assertEquals(5, employeeDTOS.size());
    }

    @Test
    void jpaProblems(){
        //ada request
        Request request =  Request.builder()
            .name("J")
            .projectName("Project A")
            .build();
        List<Employee> result;
        if(request.getName() != null && request.getProjectName() != null){
            result = employeeRepository.findByNameLikeAndProjects_ProjectName("J%", "Project A");
        }
        else if(request.getName() != null) {
            result = employeeRepository.findByNameLike(request.getName() + "%");
        } else {
            result = employeeRepository.findAll();
        }
        result.stream().map(e->e.getName())
            .forEach(System.out::println);
    }

    @Test
    void jpaExample() {
        Address address = new Address();
        address.setCity("Jakarta");

        Employee emp =  new Employee();
        emp.setId(1L);
        emp.setName("John Doe");
        emp.setAddress(address);
        emp.setName(null);
        Example<Employee> exampleEmp = Example.of(emp);
        List<Employee> employees = employeeRepository.findAll(exampleEmp);
        assertEquals(1, employees.size());
        List<EmployeeDTO> employeeDTOS = employees
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
        assertEquals(1, employeeDTOS.size());
    }


    private static Specification<Employee> getSpecification(
        EmployeeRequest request
    ) {
        var projectNameSpec = new SearchSpecification<Employee>(
            new SearchCriteria(
                "name",
                "in",
                StringUtils.collectionToCommaDelimitedString(request.getName()),
                null
            )
        );
        var meetingNameSpec = new SearchSpecification<Employee>(
            new SearchCriteria(
                "meetingName",
                "in",
                StringUtils.collectionToCommaDelimitedString(request.getMeetingNames()),
                "meetings"
            )
        );

        return Specification.where(projectNameSpec
            .and(meetingNameSpec));
    }
    @Test
    void jpaSpecification(){
        EmployeeRequest request = EmployeeRequest.builder()
            .name(Set.of("Audrey", "John Doe"))
            .meetingNames(Set.of("Meeting X"))
            .build();
        var employees = employeeRepository.findAll(getSpecification(request));
        List<EmployeeDTO> employeeDTOS = employees
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
    }

    @Test
    void jpaQueryAnnotation(){
        List<Employee> employees = employeeRepository.findEmployeesByMeetingName("Meeting X");
        assertEquals(3, employees.size());
        List<EmployeeDTO> employeeDTOS = employees
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
        assertEquals(3, employeeDTOS.size());
    }

    @Test
    void jpaQueryAnnotationNative(){
        List<Employee> employees = employeeRepository.findEmployeesByMeetingNameNative("Meeting X");
        assertEquals(3, employees.size());
        List<EmployeeDTO> employeeDTOS = employees
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
        assertEquals(3, employeeDTOS.size());
    }

    @Test
    void jpaQueryAnnotationNativeWithInterface(){
        List<EmployeeMeetingProjection> result = employeeRepository.findEmployeeAndMeetingNamesByMeetingName("Meeting X");
        assertEquals(3, result.size());
    }

    @Test
    void jpaCustomRepository(){
        List<Object[]> results = employeeCustomRepository.findCustomEmployeeByMeetingName("Meeting X");

        for (Object[] result : results) {
            String employeeName = (String) result[0];
            String meetingName = (String) result[1];
            System.out.println(employeeName + "and "+meetingName);
        }
    }

    @Test
    void jpaOldSkullRepository(){
        List<Employee> results = employeeCustomRepository.findOldSkullEmployeeByMeetingName("Meeting X");
//        List<Employee> results = employeeCustomRepository.findOldSkullEmployeeByMeetingName(null);
        results.stream().map(Employee::getName).forEach(System.out::println);
                List<EmployeeDTO> employeeDTOS = results
            .stream()
            .map(EmployeeMapper::toDto)
            .collect(Collectors.toList());
        assertNotNull(employeeDTOS);
        assertEquals(3, employeeDTOS.size());
    }
}
