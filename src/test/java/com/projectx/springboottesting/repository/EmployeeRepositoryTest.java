package com.projectx.springboottesting.repository;

import com.projectx.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        this.employee = Employee.builder()
                .firstName("John")
                .lastName("Luther")
                .email("JohnLuther@gmail.com").build();
    }

    //BDD style
    @Test
    @DisplayName("Test the save employee operation")
    public void givenEmployee_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Luther")
                .email("JohnLuther@gmail.com").build();*/

        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit test for get all employees operation
    @Test
    @DisplayName("Test for get all employee operation")
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        //given - precondition or setup

        Employee employee1 = Employee.builder()
                .firstName("Tom")
                .lastName("Cruise")
                .email("tomcruise@gmail.com").build();

        employeeRepository.saveAll(List.of(employee, employee1));

        //when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();


        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test for get employee by id operation")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee savedEmployee = employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        var employeeDB = employeeRepository.findById(savedEmployee.getId());

        //then - verify the output
        assertThat(employeeDB).isPresent();
    }

    @Test
    @DisplayName("Test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        var employeeDB = employeeRepository.findByEmail("JohnLuther@gmail.com");
        System.out.println(employeeDB);
        //then - verify the output
        assertThat(employeeDB).isPresent();
    }

    @Test
    @DisplayName("Test for update employee operation")
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
        employeeDB.setFirstName("Martin");
        employeeDB.setEmail("MartinLuther@gmail.com");
        Employee updatedEmployee = employeeRepository.save(employeeDB);

        //then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Martin");
        assertThat(updatedEmployee.getEmail()).isEqualTo("MartinLuther@gmail.com");
    }

    @Test
    @DisplayName("Test for delete employee operation")
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        employeeRepository.delete(employee);
        var employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    @DisplayName("Test for custom query using JPQL with index params")
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastName_thenReturnEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByFirstNameAndLastName("John", "Luther");

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @Test
    @DisplayName("Test for custom query using JPQL with named params")
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastNameNNamedParams_thenReturnEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByFirstNameAndLastNameNNamedParams("John", "Luther");

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @Test
    @DisplayName("Test for custom query using native SQL with index params")
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByNativeSQL("John", "Luther");

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }
}
