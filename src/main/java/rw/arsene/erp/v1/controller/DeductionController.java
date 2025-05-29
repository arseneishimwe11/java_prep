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
import rw.arsene.erp.v1.dto.DeductionDTO;
import rw.arsene.erp.v1.service.DeductionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deductions")
@RequiredArgsConstructor
@Tag(name = "Deduction Management", description = "APIs for managing deductions")
public class DeductionController {
    
    private final DeductionService deductionService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Create deduction", description = "Creates a new deduction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Deduction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Deduction already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DeductionDTO> createDeduction(@Valid @RequestBody DeductionDTO deductionDTO) {
        DeductionDTO createdDeduction = deductionService.createDeduction(deductionDTO);
        return new ResponseEntity<>(createdDeduction, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get deduction by ID", description = "Retrieves a deduction by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deduction found"),
        @ApiResponse(responseCode = "404", description = "Deduction not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DeductionDTO> getDeductionById(
            @Parameter(description = "Deduction ID") @PathVariable Long id) {
        DeductionDTO deduction = deductionService.getDeductionById(id);
        return ResponseEntity.ok(deduction);
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get deduction by code", description = "Retrieves a deduction by its code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deduction found"),
        @ApiResponse(responseCode = "404", description = "Deduction not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DeductionDTO> getDeductionByCode(
            @Parameter(description = "Deduction code") @PathVariable String code) {
        DeductionDTO deduction = deductionService.getDeductionByCode(code);
        return ResponseEntity.ok(deduction);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get all deductions", description = "Retrieves all deductions with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deductions retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<DeductionDTO>> getAllDeductions(Pageable pageable) {
        Page<DeductionDTO> deductions = deductionService.getAllDeductions(pageable);
        return ResponseEntity.ok(deductions);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get active deductions", description = "Retrieves all active deductions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active deductions retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<DeductionDTO>> getActiveDeductions() {
        List<DeductionDTO> deductions = deductionService.getActiveDeductions();
        return ResponseEntity.ok(deductions);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Search deductions", description = "Searches deductions by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<DeductionDTO>> searchDeductions(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            Pageable pageable) {
        Page<DeductionDTO> deductions = deductionService.searchDeductions(keyword, pageable);
        return ResponseEntity.ok(deductions);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Update deduction", description = "Updates an existing deduction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deduction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Deduction not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DeductionDTO> updateDeduction(
            @Parameter(description = "Deduction ID") @PathVariable Long id,
            @Valid @RequestBody DeductionDTO deductionDTO) {
        DeductionDTO updatedDeduction = deductionService.updateDeduction(id, deductionDTO);
        return ResponseEntity.ok(updatedDeduction);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Activate deduction", description = "Activates a deduction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deduction activated successfully"),
        @ApiResponse(responseCode = "404", description = "Deduction not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DeductionDTO> activateDeduction(
            @Parameter(description = "Deduction ID") @PathVariable Long id) {
        DeductionDTO deduction = deductionService.activateDeduction(id);
        return ResponseEntity.ok(deduction);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Deactivate deduction", description = "Deactivates a deduction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deduction deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Deduction not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DeductionDTO> deactivateDeduction(
            @Parameter(description = "Deduction ID") @PathVariable Long id) {
        DeductionDTO deduction = deductionService.deactivateDeduction(id);
        return ResponseEntity.ok(deduction);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete deduction", description = "Deletes a deduction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Deduction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Deduction not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteDeduction(
            @Parameter(description = "Deduction ID") @PathVariable Long id) {
        deductionService.deleteDeduction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if code exists", description = "Checks if a deduction with the given code exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByCode(
            @Parameter(description = "Deduction code") @PathVariable String code) {
        boolean exists = deductionService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/name/{deductionName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if name exists", description = "Checks if a deduction with the given name exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByDeductionName(
            @Parameter(description = "Deduction name") @PathVariable String deductionName) {
        boolean exists = deductionService.existsByDeductionName(deductionName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count active deductions", description = "Counts active deductions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countActiveDeductions() {
        long count = deductionService.countActiveDeductions();
        return ResponseEntity.ok(count);
    }
}
