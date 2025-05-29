package rw.arsene.erp.v1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.entity.Deduction;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.EmploymentDetails;
import rw.arsene.erp.v1.entity.RoleEntity;
import rw.arsene.erp.v1.enums.EmployeeStatus;
import rw.arsene.erp.v1.enums.EmploymentStatus;
import rw.arsene.erp.v1.enums.Role;
import rw.arsene.erp.v1.repository.DeductionRepository;
import rw.arsene.erp.v1.repository.EmployeeRepository;
import rw.arsene.erp.v1.repository.EmploymentDetailsRepository;
import rw.arsene.erp.v1.repository.RoleRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentDetailsRepository employmentDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");

        // 1. Initialize Roles
        initializeRoles();

        // 2. Initialize Deductions
        initializeDeductions();

        // 3. Initialize Employees and Employment Details
        initializeEmployees();

        logger.info("Sample data initialization complete.");
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            logger.info("Initializing roles...");
            roleRepository.saveAll(List.of(
                    new RoleEntity(Role.ROLE_ADMIN),
                    new RoleEntity(Role.ROLE_MANAGER),
                    new RoleEntity(Role.ROLE_EMPLOYEE)
            ));
            logger.info("Roles initialized.");
        }
    }

    private void initializeDeductions() {
        if (deductionRepository.count() == 0) {
            logger.info("Initializing deductions...");
            deductionRepository.saveAll(List.of(
                    new Deduction(null, "Employee Tax", new BigDecimal("30.00")),
                    new Deduction(null, "Pension", new BigDecimal("6.00")),
                    new Deduction(null, "Medical Insurance", new BigDecimal("5.00")),
                    new Deduction(null, "Housing", new BigDecimal("14.00")),
                    new Deduction(null, "Transport", new BigDecimal("14.00")),
                    new Deduction(null, "Others", new BigDecimal("5.00"))
            ));
            logger.info("Deductions initialized.");
        }
    }

    private void initializeEmployees() {
        if (employeeRepository.count() == 0) {
            logger.info("Initializing employees and employment details...");

            RoleEntity adminRole = roleRepository.findByName(Role.ROLE_ADMIN).orElseThrow();
            RoleEntity managerRole = roleRepository.findByName(Role.ROLE_MANAGER).orElseThrow();
            RoleEntity employeeRole = roleRepository.findByName(Role.ROLE_EMPLOYEE).orElseThrow();

            // Admin User
            Employee admin = new Employee(null, "Admin", "User", "admin@erp.gov.rw", passwordEncoder.encode("password"), Set.of(adminRole), "0788111111", new Date(), EmployeeStatus.ACTIVE);
            employeeRepository.save(admin);
            createEmploymentDetails(admin, "IT", "System Admin", new BigDecimal("150000"));

            // Manager User
            Employee manager = new Employee(null, "Manager", "User", "manager@erp.gov.rw", passwordEncoder.encode("password"), Set.of(managerRole), "0788222222", new Date(), EmployeeStatus.ACTIVE);
            employeeRepository.save(manager);
            createEmploymentDetails(manager, "Finance", "Finance Manager", new BigDecimal("120000"));

            // Employee User 1
            Employee emp1 = new Employee(null, "John", "Doe", "john.doe@erp.gov.rw", passwordEncoder.encode("password"), Set.of(employeeRole), "0788333333", new Date(), EmployeeStatus.ACTIVE);
            employeeRepository.save(emp1);
            createEmploymentDetails(emp1, "HR", "HR Officer", new BigDecimal("70000"));

            // Employee User 2 (Inactive)
            Employee emp2 = new Employee(null, "Jane", "Smith", "jane.smith@erp.gov.rw", passwordEncoder.encode("password"), Set.of(employeeRole), "0788444444", new Date(), EmployeeStatus.DISABLED);
            employeeRepository.save(emp2);
            createEmploymentDetails(emp2, "Procurement", "Procurement Officer", new BigDecimal("65000"), EmploymentStatus.INACTIVE);

            logger.info("Employees and employment details initialized.");
        }
    }

    private void createEmploymentDetails(Employee employee, String department, String position, BigDecimal baseSalary) {
        createEmploymentDetails(employee, department, position, baseSalary, EmploymentStatus.ACTIVE);
    }

    private void createEmploymentDetails(Employee employee, String department, String position, BigDecimal baseSalary, EmploymentStatus status) {
        EmploymentDetails details = new EmploymentDetails();
        details.setEmployee(employee);
        details.setDepartment(department);
        details.setPosition(position);
        details.setBaseSalary(baseSalary);
        details.setStatus(status);
        details.setJoiningDate(new Date()); // Use current date for simplicity
        employmentDetailsRepository.save(details);
    }
}
