package rw.arsene.erp.v1.controller;

import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.service.PayslipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payslips")
public class PayslipController {

    @Autowired
    private PayslipService payslipService;

    // Get specific payslip by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(	ADMIN	) or hasRole(	MANAGER	) or @securityService.isPayslipOwner(authentication, #id)") // Requires SecurityService bean
    public ResponseEntity<PayslipDTO> getPayslipById(@PathVariable String id) {
        return payslipService.getPayslipById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get payslips for a specific employee
    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasRole(	ADMIN	) or hasRole(	MANAGER	) or @securityService.isOwner(authentication, #employeeCode)") // Requires SecurityService bean
    public ResponseEntity<List<PayslipDTO>> getPayslipsByEmployee(@PathVariable String employeeCode) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByEmployee(employeeCode);
        return ResponseEntity.ok(payslips);
    }

    // Get payslips for a specific month/year (Admin/Manager only)
    @GetMapping("/period/{year}/{month}")
    @PreAuthorize("hasRole(	ADMIN	) or hasRole(	MANAGER	)")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByMonthYear(@PathVariable int year, @PathVariable int month) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByMonthYear(month, year);
        return ResponseEntity.ok(payslips);
    }

    // Get all payslips (Admin/Manager only)
    @GetMapping
    @PreAuthorize("hasRole(	ADMIN	) or hasRole(	MANAGER	)")
    public ResponseEntity<List<PayslipDTO>> getAllPayslips() {
        List<PayslipDTO> payslips = payslipService.getAllPayslips();
        return ResponseEntity.ok(payslips);
    }

    // Endpoint to update status (e.g., approve) is handled by PayrollController
}
