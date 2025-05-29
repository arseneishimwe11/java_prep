package rw.arsene.erp.v1.service;

import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.entity.Payslip;

import java.util.List;
import java.util.Optional;

public interface PayslipService {
    // Note: Payslip creation is usually part of Payroll generation, not direct CRUD
    // Optional<PayslipDTO> createPayslip(PayslipDTO payslipDTO); // Likely not needed

    Optional<PayslipDTO> getPayslipById(String id);
    Optional<PayslipDTO> getPayslipByEmployeeMonthYear(String employeeCode, int month, int year);
    List<PayslipDTO> getPayslipsByEmployee(String employeeCode);
    List<PayslipDTO> getPayslipsByMonthYear(int month, int year);
    List<PayslipDTO> getAllPayslips();
    // Update might be needed for status change (e.g., approval)
    PayslipDTO updatePayslipStatus(String id, String status); // Status should be enum
    // Delete might not be applicable, maybe just update status?
    // void deletePayslip(String id);

    // Mappers
    PayslipDTO mapToDTO(Payslip payslip);
    Payslip mapToEntity(PayslipDTO payslipDTO); // Less likely needed directly
}
