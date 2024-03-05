package com.projectx.springboottesting.repository;

import com.projectx.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // define custom query using JPQL with index parameters
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    // define custom query using JPQL with named parameters
    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    Employee findByFirstNameAndLastNameNNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // define custom query using native SQL with index parameters
    @Query(value = "select * from employee e where e.first_name =?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);
}
