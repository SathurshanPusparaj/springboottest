package com.projectx.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectx.springboottesting.model.Employee;
import com.projectx.springboottesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Testcontainers
public class EmployeeControllerITWithTestContainerContainer extends AbstractContainerBaseTest {

/*    @Container
    private static final MySQLContainer SQL_CONTAINER = new MySQLContainer("mysql:8.1.0")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("ems");

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", SQL_CONTAINER::getPassword);
    }*/

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Junit test for createEmployee (POST)")
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Bill")
                .lastName("Gates")
                .email("billgates@gmail.com")
                .build();

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())//output the http request and response
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName("Junit test for getAllEmployee (GET)")
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnAllEmployee() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder()
                .firstName("Martin")
                .lastName("Fisher")
                .email("martinfisher@gmail.com")
                .build());
        employeeList.add(Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("tonyStark@gmail.com")
                .build());

        employeeRepository.saveAll(employeeList);

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @Test
    @DisplayName("Junit test for getEmployeeById - (GET) - Valid(200)")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("Tony")))//JSON PATH
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Stark")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("tonystark@gmail.com")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Junit test for getEmployeeById - (GET) - Invalid(404)")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Junit test for updateEmployeeById - (PUT) - Valid(200)")
    public void givenEmployeeIdAndUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup

        Employee employee = Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();
        employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Wick")
                .email("johnwick@gmail.com")
                .build();

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    @Test
    @DisplayName("Junit test for updateEmployeeById - (PUT) - Invalid(404)")
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();
        employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Wick")
                .email("johnwick@gmail.com")
                .build();


        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Junit for delete employee - (DELETE) - valid(200)")
    public void givenEmployeeId_whenDeleteById_thenReturnOk() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
