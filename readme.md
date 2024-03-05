### What is Unit Testing
Unit testing involves the testing of each unit or an individual component of the software
application.
The purpose is to validate that each unit of the software code performs as expected
Unit testing is done during development (coding phase) of an application by the
developers
Unit may be an individual function, method, procedure, module and object

### Integration Testing
As the name suggests, integration tests focus on integrating different layers of the
application. That also means no mocking is involved.
Basically, we write integration tests for testing a feature which may involve
interaction with multiple components.

### Unit Test Naming Conventions
```
1. MethodName_StateUnderTest_ExpectedBehavior:
   isAdult_AgeLessThan18_False
   withdrawMoney_InvalidAccount_ExceptionThrow n
   admitStudent_MissingMandatoryFields_FailToAdmit

2. MethodName_ExpectedBehavior_StateUnderTest
   isAdult_False_AgeLessThan1 8
   withdrawMoney_ThrowsException_IfAccountIsInvalid
   admitStudent_FailToAdmit_IfMandatoryFieldsAreMissing
   Unit Test Naming Conventions

3. test[MethodName]:
       testSaveEmployee
       testGetEmployeeById
       testUpdateEmployee

4. test[Feature being tested]
   testFailToWithdrawMoneyIfAccountIsInvalid
   testStudentIsNotAdmittedIfMandatoryFieldsAreMissing
   Unit Test Naming Conventions

5. Should_ExpectedBehavior_When_StateUnderTest:
   Should_ThrowException_When_AgeLessThan18
   Should_FailToWithdrawMoney_ForInvalidAccount
   Should_FailToAdmit_IfMandatoryFieldsAreMissing

6. When_StateUnderTest_Expect_ExpectedBehavior:
   When_AgeLessThan18_Expect_isAdultAsFalse
   When_InvalidAccount_Expect_WithdrawMoneyToFail
   When_MandatoryFieldsAreMissing_Expect_StudentAdmissionToFail
   Unit Test Naming Conventions

7. givenPreconditions_whenStateUnderTest_thenExpectedBehavior:
   givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee
   givenEmployeesList_whenFindAll_thenReturnListOfEmployee
   givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployees
   givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject
   givenInValidEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject
```

#### SpringBootStarterTest provides following dependencies
JUnit, Mockito, Hamcrest, AssertJ, JSONAssert, and JsonPath.


### @DataJpaTest

Spring Boot provides the @DataJpaTest annotation to test the persistence
layer components that will autoconfigure in-memory embedded database
for testing purposes.

The @DataJpaTest annotation does not load other Spring beans
(@Components, @Controller, @Service, and annotated beans) into
ApplicationContext.  

By default, it scans for @Entity classes and configures Spring Data JPA
repositories annotated with @Repository annotation

***By default, tests annotated with @DataJpaTest are transactional and roll
back at the end of each test.*** To disable this feature add @RollBackTest

This will also help to test other JPA-related components such as:
Datasource, JdbcTemplate, EntityManager, Repository

### @WebMvcTest
SpringBoot provides @WebMvcTest annotation to test Spring MVC Controllers.
Also, @WebMvcTest based tests runs faster as it will load only the specified controller
and its dependencies only without loading the entire application.

Spring Boot instantiates only the web layer rather than the whole application context. In
an application with multiple controllers, you can even ask for only one to be
instantiated by using, for example, @WebMvcTest(HomeController.class).

### @SpringBootTest
Spring Boot provides @SpringBootTest annotation for Integration testing. This
annotation creates an application context and loads full application context.
@SpringBootTest will bootstrap the full application context, which means we
can @Autowired any bean that's picked up by component scanning into our test.
Integration testing - @SpringBootTest

It starts the embedded server, creates a web environment and then enables @Test methods to
do integration testing.

By default, @SpringBootTest  does not start a server. We need to add attribute
webEnvironment to further refine how your tests run. It has several options:
1. MOCK(Default): Loads a web ApplicationContext and provides a mock web
environment
2. RANDOM_PORT: Loads a WebServerApplicationContext and provides a real web
environment. The embedded server is started and listen on a random port. This is the
one should be used for the integration test
3. DEFINED_PORT: Loads a WebServerApplicationContext and provides a real web
environment.
4. NONE: Loads an ApplicationContext by using SpringApplication but does not
provide any web environment

