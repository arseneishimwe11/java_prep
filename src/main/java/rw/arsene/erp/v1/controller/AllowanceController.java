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
import java.util.List;

@RestController
@RequestMapping("/api/v1/allowances")
@RequiredArgsConstructor
@Tag(name = "Allowance Management", description = "APIs for managing allowances")
public class AllowanceController {
    
    private final AllowanceService allowanceService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Create allowance", description = "Creates a new allowance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Allowance created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Allowance already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<AllowanceDTO> createAllowance(@Valid @RequestBody AllowanceDTO allowanceDTO) {
        AllowanceDTO createdAllowance = allowanceService.createAllowance(allowanceDTO);
        return new ResponseEntity<>(createdAllowance, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Update allowance", description = "Updates an existing allowance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Allowance updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Allowance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<AllowanceDTO> updateAllowance(
            @Parameter(description = "Allowance ID") @PathVariable Long id,
            @Valid @RequestBody AllowanceDTO allowanceDTO) {
        AllowanceDTO updatedAllowance = allowanceService.updateAllowance(id, allowanceDTO);
        return ResponseEntity.ok(updatedAllowance);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get allowance by ID", description = "Retrieves an allowance by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Allowance found"),
        @ApiResponse(responseCode = "404", description = "Allowance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<AllowanceDTO> getAllowanceById(
            @Parameter(description = "Allowance ID") @PathVariable Long id) {
        AllowanceDTO allowance = allowanceService.getAllowanceById(id);
        return ResponseEntity.ok(allowance);
    }
    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get allowance by code", description = "Retrieves an allowance by its code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Allowance found"),
        @ApiResponse(responseCode = "404", description = "Allowance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<AllowanceDTO> getAllowanceByCode(
            @Parameter(description = "Allowance code") @PathVariable String code) {
        AllowanceDTO allowance = allowanceService.getAllowanceByCode(code);
        return ResponseEntity.ok(allowance);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get all allowances", description = "Retrieves all allowances with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Allowances retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<AllowanceDTO>> getAllAllowances(Pageable pageable) {
        Page<AllowanceDTO> allowances = allowanceService.getAllAllowances(pageable);
        return ResponseEntity.ok(allowances);
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get active allowances", description = "Retrieves all active allowances")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active allowances retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<AllowanceDTO>> getActiveAllowances() {
        List<AllowanceDTO> allowances = allowanceService.getActiveAllowances();
        return ResponseEntity.ok(allowances);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Search allowances", description = "Searches allowances by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<AllowanceDTO>> searchAllowances(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            Pageable pageable) {
        Page<AllowanceDTO> allowances = allowanceService.searchAllowances(keyword, pageable);
        return ResponseEntity.ok(allowances);
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Activate allowance", description = "Activates an allowance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Allowance activated successfully"),
        @ApiResponse(responseCode = "404", description = "Allowance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<AllowanceDTO> activateAllowance(
            @Parameter(description = "Allowance ID") @PathVariable Long id) {
        AllowanceDTO allowance = allowanceService.activateAllowance(id);
        return ResponseEntity.ok(allowance);
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Deactivate allowance", description = "Deactivates an allowance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Allowance deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Allowance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<AllowanceDTO> deactivateAllowance(
            @Parameter(description = "Allowance ID") @PathVariable Long id) {
        AllowanceDTO allowance = allowanceService.deactivateAllowance(id);
        return ResponseEntity.ok(allowance);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete allowance", description = "Deletes an allowance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Allowance deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Allowance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteAllowance(
            @Parameter(description = "Allowance ID") @PathVariable Long id) {
        allowanceService.deleteAllowance(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/exists/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if code exists", description = "Checks if an allowance with the given code exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByCode(
            @Parameter(description = "Allowance code") @PathVariable String code) {
        boolean exists = allowanceService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/name/{allowanceName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Check if name exists", description = "Checks if an allowance with the given name exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Boolean> existsByAllowanceName(
            @Parameter(description = "Allowance name") @PathVariable String allowanceName) {
        boolean exists = allowanceService.existsByAllowanceName(allowanceName);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count active allowances", description = "Counts active allowances")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countActiveAllowances() {
        long count = allowanceService.countActiveAllowances();
        return ResponseEntity.ok(count);
    }
}