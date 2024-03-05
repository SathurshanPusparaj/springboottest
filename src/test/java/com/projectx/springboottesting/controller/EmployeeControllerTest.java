package com.projectx.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectx.springboottesting.model.Employee;
import com.projectx.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * .willAnswer(invocation -> invocation.getArgument(0)):
     *
     * .willAnswer: This part, from the Mockito library, defines the behavior of the mock when the method is called.
     * It specifies what the mock should do when the saveEmployee method is called.
     * invocation -> invocation.getArgument(0): This is a lambda expression that defines how the mock should respond.
     * It simply returns the first argument passed to the saveEmployee method.
     * In essence, the mock behaves like it saves the received Employee object but doesn't perform any actual saving logic.
     */
    @Test
    @DisplayName("Junit test for createEmployee (POST)")
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Bill")
                .lastName("Gates")
                .email("billgates@gmail.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);

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
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("tonystark@gmail.com")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Junit test for getEmployeeById - (GET) - Invalid(404)")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

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
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Tony")
                .lastName("Stark")
                .email("tonystark@gmail.com")
                .build();


        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Wick")
                .email("johnwick@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
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

        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Wick")
                .email("johnwick@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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
        long employeeId = 1L;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
