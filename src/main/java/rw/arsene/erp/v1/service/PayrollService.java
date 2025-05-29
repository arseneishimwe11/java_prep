package rw.arsene.erp.v1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.arsene.erp.v1.dto.PayrollGenerationDto;
import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.entity.Payslip;
import rw.arsene.erp.v1.enums.PayslipStatus;

import java.math.BigDecimal;
import java.util.List;

public interface PayrollService {
    
    PayslipDTO createPayslip(PayslipDTO payslipDTO);
    PayslipDTO updatePayslip(Long id, PayslipDTO payslipDTO);
    PayslipDTO getPayslipById(Long id);
    
    List<PayslipDTO> getAllPayslips();
    Page<PayslipDTO> getAllPayslips(Pageable pageable);
    List<PayslipDTO> getPayslipsByEmployeeId(Long employeeId);
    Page<PayslipDTO> getPayslipsByEmployeeId(Long employeeId, Pageable pageable);
    List<PayslipDTO> getPayslipsByStatus(PayslipStatus status);
    Page<PayslipDTO> getPayslipsByStatus(PayslipStatus status, Pageable pageable);
    List<PayslipDTO> getPayslipsByPeriod(Integer month, Integer year);
    Page<PayslipDTO> getPayslipsByPeriod(Integer month, Integer year, Pageable pageable);
    
    Page<PayslipDTO> searchPayslips(String keyword, Pageable pageable);
    
    PayslipDTO approvePayslip(Long id);
    PayslipDTO rejectPayslip(Long id);
    PayslipDTO markAsPaid(Long id);
    
    List<PayslipDTO> generatePayroll(PayrollGenerationDto payrollGenerationDto);
    PayslipDTO generatePayslipForEmployee(Long employeeId, Integer month, Integer year);
    
    void deletePayslip(Long id);
    
    boolean existsByEmployeeAndPeriod(Long employeeId, Integer month, Integer year);
    
    long countByStatus(PayslipStatus status);
    long countByPeriod(Integer month, Integer year);
    
    BigDecimal getTotalGrossSalaryByPeriod(Integer month, Integer year);
    BigDecimal getTotalNetSalaryByPeriod(Integer month, Integer year);
    BigDecimal getTotalDeductionsByPeriod(Integer month, Integer year);
    
    // Internal methods for other services
    Payslip getPayslipEntityById(Long id);
}
