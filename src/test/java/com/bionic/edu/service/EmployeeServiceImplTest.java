package com.bionic.edu.service;

import com.bionic.edu.entity.Employee;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmployeeServiceImplTest {

    private EmployeeService employeeService;
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/webapp/WEB-INF/applicationContext.xml");
        employeeService = context.getBean(EmployeeService.class);
        roleService = context.getBean(RoleService.class);
    }

    @Test
    public void testFindById() throws Exception {
        Employee employee = employeeService.findById(1);
        assertNotNull(employee);
        System.out.println(employee);
        assertEquals(1, employee.getId());
    }

    @Test
    public void testFindByEmail() throws Exception {
        Employee employee = employeeService.findByEmail("super@erestaurant.com");
        assertNotNull(employee);
        assertEquals("super@erestaurant.com", employee.getEmail());
        assertEquals("Dmytro Burdyga", employee.getName());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Employee> employees = employeeService.findAll();
        assertNotNull(employees);
        assertEquals(5, employees.size());
    }

    @Test
    public void testSave_NotNull() throws Exception {
        Employee employee = new Employee("testName", "testAdd@email.com", "testPass", new Date(1984-06-27), new Date(2013-04-02), true, roleService.findById(1));
        employeeService.save(employee);
        int id = employee.getId();
        assertNotNull(employeeService.findById(id));
        assertNotNull(employeeService.findByEmail("testAdd@email.com"));
    }

    @Test
    public void testSave_listSize() throws Exception {
        Employee employee = new Employee("testName", "testAdd@email.com", "testPass", new Date(1984-06-27), new Date(2013-04-02), true, roleService.findById(1));
        List<Employee> list1 = employeeService.findAll();
        employeeService.save(employee);
        List<Employee> list2 = employeeService.findAll();
        assertEquals(list2.size() - list1.size(), 1);
    }

    @Test
    public void testUpdate() throws Exception {
        Employee employee = employeeService.findById(1);
        employee.setName("Dima TestUpdate");
        employeeService.save(employee);
        assertEquals("Dima TestUpdate", employeeService.findById(1).getName());
    }

    @Test
    public void testDelete() throws Exception {
        Employee employee = employeeService.findById(2);
        employee.setEmail("testDelete@email.com");
        employeeService.save(employee);
        int id = employee.getId();
        employeeService.delete(id);
        assertEquals(null, employeeService.findById(id));
    }

    @Test
    public void testSignIn() throws Exception {
        try {
            employeeService.signIn("elena.bakhmach@gmail.com", "wrongPass");
        } catch (AssertionError er) {
            assertEquals(1, 1);
        }
        employeeService.signIn("elena.bakhmach@gmail.com", "pass3");
    }

    @Test
    public void testSetReadiness() throws Exception {
        Employee employee = employeeService.signIn("admin@erestaurant.com", "pass2");
        employeeService.signIn("admin@erestaurant.com", "pass2");
        assertEquals("Igor Himchenko", employee.getName());
        assertEquals("ADMIN", employee.getRole().getName());
    }
}