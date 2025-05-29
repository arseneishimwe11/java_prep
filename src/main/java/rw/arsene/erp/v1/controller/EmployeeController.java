package rw.arsene.erp.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.arsene.erp.v1.dto.EmployeeDTO;
import rw.arsene.erp.v1.dto.SignupRequest;
import rw.arsene.erp.v1.enums.EmployeeStatus;
import rw.arsene.erp.v1.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Create new employee", description = "Creates a new employee in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Employee already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody SignupRequest signupRequest) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(signupRequest);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Update employee", description = "Updates an existing employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate data"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get employee by code", description = "Retrieves an employee by their code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> getEmployeeByCode(
            @Parameter(description = "Employee code") @PathVariable String code) {
        EmployeeDTO employee = employeeService.getEmployeeByCode(code);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get employee by email", description = "Retrieves an employee by their email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(
            @Parameter(description = "Employee email") @PathVariable String email) {
        EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get all employees", description = "Retrieves all employees with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(Pageable pageable) {
        Page<EmployeeDTO> employees = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get employees by status", description = "Retrieves employees by their status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByStatus(
            @Parameter(description = "Employee status") @PathVariable EmployeeStatus status,
            Pageable pageable) {
        Page<EmployeeDTO> employees = employeeService.getEmployeesByStatus(status, pageable);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Search employees", description = "Searches employees by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            Pageable pageable) {
        Page<EmployeeDTO> employees = employeeService.searchEmployees(keyword, pageable);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/active-with-employment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get active employees with employment", description = "Retrieves active employees with active employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<EmployeeDTO>> getActiveEmployeesWithActiveEmployment() {
        List<EmployeeDTO> employees = employeeService.getActiveEmployeesWithActiveEmployment();
        return ResponseEntity.ok(employees);
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Activate employee", description = "Activates an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee activated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> activateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        EmployeeDTO employee = employeeService.activateEmployee(id);
        return ResponseEntity.ok(employee);
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Deactivate employee", description = "Deactivates an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> deactivateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        EmployeeDTO employee = employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(employee);
    }
    
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Suspend employee", description = "Suspends an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee suspended successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmployeeDTO> suspendEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        EmployeeDTO employee = employeeService.suspendEmployee(id);
        return ResponseEntity.ok(employee);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employee", description = "Deletes an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/exists/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if email exists", description = "Checks if an employee with the given email exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByEmail(
            @Parameter(description = "Employee email") @PathVariable String email) {
        boolean exists = employeeService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if code exists", description = "Checks if an employee with the given code exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByCode(
            @Parameter(description = "Employee code") @PathVariable String code) {
        boolean exists = employeeService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count employees by status", description = "Counts employees by their status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countByStatus(
            @Parameter(description = "Employee status") @PathVariable EmployeeStatus status) {
        long count = employeeService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
}
