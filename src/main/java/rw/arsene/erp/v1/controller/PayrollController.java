package rw.arsene.erp.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.arsene.erp.v1.dto.MessageResponse;
import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.service.PayrollService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private static final Logger logger = LoggerFactory.getLogger(PayrollController.class);

    @Autowired
    private PayrollService payrollService;

    // Endpoint to generate payroll for a specific month/year
    @PostMapping("/generate/{year}/{month}")
    @PreAuthorize("hasRole(	MANAGER	)")
    public ResponseEntity<?> generatePayroll(@PathVariable int year, @PathVariable int month) {
        try {
            logger.info("Received request to generate payroll for {}/{}", month, year);
            List<PayslipDTO> generatedPayslips = payrollService.generatePayroll(month, year);
            if (generatedPayslips.isEmpty()) {
                return ResponseEntity.ok(new MessageResponse("Payroll generation complete. No new payslips generated (possibly already exist or no active employees)."));
            }
            return ResponseEntity.ok(generatedPayslips);
        } catch (Exception e) {
            logger.error("Error during payroll generation for {}/{}: {}", month, year, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error generating payroll: " + e.getMessage()));
        }
    }

    // Endpoint to approve a specific payslip by ID
    @PutMapping("/approve/payslip/{payslipId}")
    @PreAuthorize("hasRole(	ADMIN	)")
    public ResponseEntity<?> approvePayslip(@PathVariable String payslipId) {
        try {
            logger.info("Received request to approve payslip ID: {}", payslipId);
            PayslipDTO approvedPayslip = payrollService.approvePayslip(payslipId);
            return ResponseEntity.ok(approvedPayslip);
        } catch (IllegalStateException e) {
            logger.error("Illegal state while approving payslip {}: {}", payslipId, e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error approving payslip {}: {}", payslipId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error approving payslip: " + e.getMessage()));
        }
    }

    // Endpoint to approve all pending payslips for a specific month/year
    @PutMapping("/approve/period/{year}/{month}")
    @PreAuthorize("hasRole(	ADMIN	)")
    public ResponseEntity<?> approvePayrollPeriod(@PathVariable int year, @PathVariable int month) {
        try {
            logger.info("Received request to approve payroll for period {}/{}", month, year);
            List<PayslipDTO> approvedPayslips = payrollService.approvePayroll(month, year);
             if (approvedPayslips.isEmpty()) {
                return ResponseEntity.ok(new MessageResponse("Payroll approval complete for " + month + "/" + year + ". No payslips were pending approval."));
            }
            return ResponseEntity.ok(approvedPayslips);
        } catch (Exception e) {
            logger.error("Error during payroll approval for {}/{}: {}", month, year, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error approving payroll for period: " + e.getMessage()));
        }
    }

    @GetMapping("/payslips/count/period/{month}/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count payslips by period", description = "Counts payslips by period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countByPeriod(
            @Parameter(description = "Month (1-12)") @PathVariable Integer month,
            @Parameter(description = "Year") @PathVariable Integer year) {
        long count = payrollService.countByPeriod(month, year);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/payslips/total-salary/period/{month}/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get total salary by period", description = "Gets total salary for a specific period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total salary retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<BigDecimal> getTotalSalaryByPeriod(
            @Parameter(description = "Month (1-12)") @PathVariable Integer month,
            @Parameter(description = "Year") @PathVariable Integer year) {
        BigDecimal totalSalary = payrollService.getTotalSalaryByPeriod(month, year);
        return ResponseEntity.ok(totalSalary);
    }
    
    @GetMapping("/payslips/total-deductions/period/{month}/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get total deductions by period", description = "Gets total deductions for a specific period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total deductions retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<BigDecimal> getTotalDeductionsByPeriod(
            @Parameter(description = "Month (1-12)") @PathVariable Integer month,
            @Parameter(description = "Year") @PathVariable Integer year) {
        BigDecimal totalDeductions = payrollService.getTotalDeductionsByPeriod(month, year);
        return ResponseEntity.ok(totalDeductions);
    }
}
