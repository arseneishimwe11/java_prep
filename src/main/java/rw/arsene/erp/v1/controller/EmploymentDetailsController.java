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
import rw.arsene.erp.v1.dto.EmploymentDetailsDTO;
import rw.arsene.erp.v1.enums.EmploymentStatus;
import rw.arsene.erp.v1.service.EmploymentDetailsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employment-details")
@RequiredArgsConstructor
@Tag(name = "Employment Details Management", description = "APIs for managing employment details")
public class EmploymentDetailsController {
    
    private final EmploymentDetailsService employmentDetailsService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Create employment details", description = "Creates new employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employment details created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Employment details already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> createEmploymentDetails(@Valid @RequestBody EmploymentDetailsDTO employmentDetailsDTO) {
        EmploymentDetailsDTO createdEmploymentDetails = employmentDetailsService.createEmploymentDetails(employmentDetailsDTO);
        return new ResponseEntity<>(createdEmploymentDetails, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Update employment details", description = "Updates existing employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment details updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> updateEmploymentDetails(
            @Parameter(description = "Employment details ID") @PathVariable Long id,
            @Valid @RequestBody EmploymentDetailsDTO employmentDetailsDTO) {
        EmploymentDetailsDTO updatedEmploymentDetails = employmentDetailsService.updateEmploymentDetails(id, employmentDetailsDTO);
        return ResponseEntity.ok(updatedEmploymentDetails);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get employment details by ID", description = "Retrieves employment details by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment details found"),
        @ApiResponse(responseCode = "404", description = "Employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> getEmploymentDetailsById(
            @Parameter(description = "Employment details ID") @PathVariable Long id) {
        EmploymentDetailsDTO employmentDetails = employmentDetailsService.getEmploymentDetailsById(id);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @GetMapping("/employee/{employeeId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get active employment by employee", description = "Retrieves active employment details for an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active employment details found"),
        @ApiResponse(responseCode = "404", description = "Active employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> getActiveEmploymentByEmployeeId(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        EmploymentDetailsDTO employmentDetails = employmentDetailsService.getActiveEmploymentByEmployeeId(employeeId);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get all employment details", description = "Retrieves all employment details with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment details retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<EmploymentDetailsDTO>> getAllEmploymentDetails(Pageable pageable) {
        Page<EmploymentDetailsDTO> employmentDetails = employmentDetailsService.getAllEmploymentDetails(pageable);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get employment details by employee", description = "Retrieves all employment details for an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment details retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<EmploymentDetailsDTO>> getEmploymentDetailsByEmployeeId(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        List<EmploymentDetailsDTO> employmentDetails = employmentDetailsService.getEmploymentDetailsByEmployeeId(employeeId);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get employment details by status", description = "Retrieves employment details by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment details retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<EmploymentDetailsDTO>> getEmploymentDetailsByStatus(
            @Parameter(description = "Employment status") @PathVariable EmploymentStatus status,
            Pageable pageable) {
        Page<EmploymentDetailsDTO> employmentDetails = employmentDetailsService.getEmploymentDetailsByStatus(status, pageable);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @GetMapping("/department/{department}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get employment details by department", description = "Retrieves employment details by department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment details retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<EmploymentDetailsDTO>> getEmploymentDetailsByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        List<EmploymentDetailsDTO> employmentDetails = employmentDetailsService.getEmploymentDetailsByDepartment(department);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Search employment details", description = "Searches employment details by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<EmploymentDetailsDTO>> searchEmploymentDetails(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            Pageable pageable) {
        Page<EmploymentDetailsDTO> employmentDetails = employmentDetailsService.searchEmploymentDetails(keyword, pageable);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Activate employment", description = "Activates employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment activated successfully"),
        @ApiResponse(responseCode = "404", description = "Employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> activateEmployment(
            @Parameter(description = "Employment details ID") @PathVariable Long id) {
        EmploymentDetailsDTO employmentDetails = employmentDetailsService.activateEmployment(id);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @PatchMapping("/{id}/terminate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Terminate employment", description = "Terminates employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment terminated successfully"),
        @ApiResponse(responseCode = "404", description = "Employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> terminateEmployment(
            @Parameter(description = "Employment details ID") @PathVariable Long id) {
        EmploymentDetailsDTO employmentDetails = employmentDetailsService.terminateEmployment(id);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Suspend employment", description = "Suspends employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employment suspended successfully"),
        @ApiResponse(responseCode = "404", description = "Employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EmploymentDetailsDTO> suspendEmployment(
            @Parameter(description = "Employment details ID") @PathVariable Long id) {
        EmploymentDetailsDTO employmentDetails = employmentDetailsService.suspendEmployment(id);
        return ResponseEntity.ok(employmentDetails);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employment details", description = "Deletes employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employment details deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employment details not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteEmploymentDetails(
            @Parameter(description = "Employment details ID") @PathVariable Long id) {
        employmentDetailsService.deleteEmploymentDetails(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/exists/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if code exists", description = "Checks if employment details with the given code exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByCode(
            @Parameter(description = "Employment code") @PathVariable String code) {
        boolean exists = employmentDetailsService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/employee/{employeeId}/has-active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if employee has active employment", description = "Checks if employee has active employment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> hasActiveEmployment(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        boolean hasActive = employmentDetailsService.hasActiveEmployment(employeeId);
        return ResponseEntity.ok(hasActive);
    }
    
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count by status", description = "Counts employment details by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countByStatus(
            @Parameter(description = "Employment status") @PathVariable EmploymentStatus status) {
        long count = employmentDetailsService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/department/{department}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count by department", description = "Counts employment details by department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        long count = employmentDetailsService.countByDepartment(department);
        return ResponseEntity.ok(count);
    }
}
