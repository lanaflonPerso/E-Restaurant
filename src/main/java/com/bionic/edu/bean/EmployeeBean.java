package com.bionic.edu.bean;

import com.bionic.edu.entity.Employee;
import com.bionic.edu.entity.Role;
import com.bionic.edu.exception.BadCredentialsException;
import com.bionic.edu.exception.EmployeeNotReadyException;
import com.bionic.edu.service.EmployeeService;
import com.bionic.edu.service.RoleService;
import com.bionic.edu.util.Crypto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bionic.edu.util.GrowlMessenger.addMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

@Named
@RequestScoped
@Scope("session")
public class EmployeeBean implements Serializable {
    private static final long serialVersionUID = -6003880259950580877L;

    private String email;
    private String password;
    private String role;
    private boolean signedIn;
    private boolean sortAscending;
    private Map<String, String> idNameRoleMap;
    private Map<String, Role> idRoleMap;
    private List<Employee> employees = null;
    private Employee employee = null;
    private Employee newEmployee = null;
    @Inject
    private EmployeeService employeeService;
    @Inject
    private RoleService roleService;

    private static final Logger logger = LogManager.getLogger(EmployeeBean.class);

    @PostConstruct
    public void init() {
        employee = new Employee();
        employees = employeeService.findAll();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    public Map<String, String> getIdNameRoleMap() {
        return idNameRoleMap;
    }

    public void setIdNameRoleMap(Map<String, String> idNameRoleMap) {
        this.idNameRoleMap = idNameRoleMap;
    }

    public Map<String, Role> getIdRoleMap() {
        return idRoleMap;
    }

    public void setIdRoleMap(Map<String, Role> idRoleMap) {
        this.idRoleMap = idRoleMap;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }

    @SuppressWarnings("all")
    public void sortEmployees() {
        if (sortAscending) {
            // Ascending order
            Collections.sort(employees, (e1, e2) -> e1.getId() < e2.getId() ? -1 : 1);
            sortAscending = false;
        } else {
            // Descending order
            Collections.sort(employees, (e1, e2) -> e1.getId() > e2.getId() ? -1 : 1);
            sortAscending = true;
        }
    }

    public void refreshEmployeeList() {
        employees = employeeService.findAll();
    }

    public String addEmployee() {
        refreshRoles();
        newEmployee = new Employee();
        return "newEmployee";
    }

    public String saveEmployee() {
        newEmployee.setRole(idRoleMap.get(role));
        if (newEmployee.getRole() == null) {
            addMessage("Role is required", "Please select employee's role.", FacesMessage.SEVERITY_ERROR);
            return null;
        }
        try {
            employeeService.save(newEmployee);
        } catch (DataIntegrityViolationException | javax.persistence.PersistenceException ex) {   // <--- workaround
            addMessage("Saving Error", "This email is already used. Please choose different one.", FacesMessage.SEVERITY_ERROR);
            logger.error("\nSaving employee ERROR - Current email is already used.", " EmployeeID:" + newEmployee.getId());
            return null;
        }
        addMessage("Saved successfully", "Customer's data was successfully saved.", SEVERITY_INFO);
        return "employeeList";
    }

    public String updateEmployee(String id) {
        refreshRoles();
        newEmployee = employeeService.findById(Integer.valueOf(id));
        return "editEmployee";
    }

    public void refreshRoles() {
        idNameRoleMap = new HashMap<>();
        idRoleMap = new HashMap<>();
        List<Role> employeeRoles = roleService.findAll();
        for (Role role : employeeRoles) {
            if (role.getId() == 1)
                continue;
            idNameRoleMap.put(role.getName(), String.valueOf(role.getId()));
            idRoleMap.put(String.valueOf(role.getId()), role);
        }
    }

    public String signIn(String email, String password) {
        String decryptPass = Crypto.encrypt(password);
        try {
            employee = employeeService.signIn(email, decryptPass);
        } catch (BadCredentialsException e) {
            addMessage("Sign In Error", "Incorrect email or password. Please try again.", FacesMessage.SEVERITY_ERROR);
            logger.error("\nEmployee sign in ERROR - Incorrect email or password." + "Email:" + email + " Password:" + password);
            employee = null;
            return "employeeSignIn";
        } catch (EmployeeNotReadyException e) {
            addMessage("Sign In Error", "You account in unavailable at the moment. Please contact SuperUser.", FacesMessage.SEVERITY_ERROR);
            logger.error("\nEmployee sign in ERROR - Account blocked." + " Email:" + email);
            employee = null;
            return "employeeSignIn";
        } catch (Exception ex) {
            addMessage("Sign In Error", "Unknown error.", FacesMessage.SEVERITY_ERROR);
            logger.error("\nEmployee sign in ERROR - Unknown error." + " Email:" + email + "\n" + ex.getMessage());
            employee = null;
            return "signIn";
        }
        signedIn = true;
        logger.warn("\nEmployee " + employee.getName() + " with ID:" + employee.getId() + " signed in successfully as " + employee.getRole().getName());
        if (employee.getRole().getName().equals("SUPER_USER"))
            return "superPanel.xhtml";
        if (employee.getRole().getName().equals("ADMIN"))
            return "dishList.xhtml";
        if (employee.getRole().getName().equals("KITCHEN_STAFF"))
            return "kitchen.xhtml";
        if (employee.getRole().getName().equals("DELIVERY_STAFF"))
            return "delivery.xhtml";
        if (employee.getRole().getName().equals("BUSINESS_ANALYST"))
            return "reports.xhtml";
        return "employeeSignIn";
    }

    public String signOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        addMessage("Signed Out", "Thank you, have a good day.", FacesMessage.SEVERITY_INFO);
        logger.warn("\nEmployee ID:" + employee.getId() + " signed out.");
        return "employeeSignIn?faces-redirect=true";
    }

}

