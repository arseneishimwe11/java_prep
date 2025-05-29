package rw.arsene.erp.v1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.dto.PayrollGenerationDto;
import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.EmploymentDetails;
import rw.arsene.erp.v1.entity.Payslip;
import rw.arsene.erp.v1.enums.PayslipStatus;
import rw.arsene.erp.v1.exception.BusinessException;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.mapper.PayslipMapper;
import rw.arsene.erp.v1.repository.PayslipRepository;
import rw.arsene.erp.v1.service.EmployeeService;
import rw.arsene.erp.v1.service.EmploymentDetailsService;
import rw.arsene.erp.v1.service.PayrollService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PayrollServiceImpl implements PayrollService {
    
    private final PayslipRepository payslipRepository;
    private final PayslipMapper payslipMapper;
    private final EmployeeService employeeService;
    private final EmploymentDetailsService employmentDetailsService;
    
    // Tax and deduction rates (these could be configurable)
    private static final BigDecimal PENSION_RATE = new BigDecimal("0.03"); // 3%
    private static final BigDecimal MEDICAL_INSURANCE_RATE = new BigDecimal("0.075"); // 7.5%
    private static final BigDecimal HOUSE_ALLOWANCE_RATE = new BigDecimal("0.15"); // 15%
    private static final BigDecimal TRANSPORT_ALLOWANCE_RATE = new BigDecimal("0.10"); // 10%
    
    @Override
    public PayslipDTO createPayslip(PayslipDTO payslipDTO) {
        log.info("Creating payslip for employee ID: {} for period {}/{}", 
                payslipDTO.getEmployeeId(), payslipDTO.getMonth(), payslipDTO.getYear());
        
        // Check if payslip already exists for this employee and period
        if (existsByEmployeeAndPeriod(payslipDTO.getEmployeeId(), payslipDTO.getMonth(), payslipDTO.getYear())) {
            throw new BusinessException("Payslip already exists for this employee and period");
        }
        
        // Validate employee exists
        Employee employee = employeeService.getEmployeeEntityById(payslipDTO.getEmployeeId());
        
        Payslip payslip = payslipMapper.toEntity(payslipDTO);
        Payslip savedPayslip = payslipRepository.save(payslip);
        
        log.info("Payslip created successfully with ID: {}", savedPayslip.getId());
        return payslipMapper.toDTO(savedPayslip);
    }
    
    @Override
    public PayslipDTO updatePayslip(Long id, PayslipDTO payslipDTO) {
        log.info("Updating payslip with ID: {}", id);
        
        Payslip existingPayslip = getPayslipEntityById(id);
        
        // Check if payslip is already approved/paid
        if (existingPayslip.getStatus() == PayslipStatus.APPROVED || 
            existingPayslip.getStatus() == PayslipStatus.PAID) {
            throw new BusinessException("Cannot update approved or paid payslip");
        }
        
        // Update fields
        existingPayslip.setBaseSalary(payslipDTO.getBaseSalary());
        existingPayslip.setHouseAmount(payslipDTO.getHouseAmount());
        existingPayslip.setTransportAmount(payslipDTO.getTransportAmount());
        existingPayslip.setGrossSalary(payslipDTO.getGrossSalary());
        existingPayslip.setEmployeeTaxed(payslipDTO.getEmployeeTaxed());
        existingPayslip.setPensionAmount(payslipDTO.getPensionAmount());
        existingPayslip.setMedicalInsurance(payslipDTO.getMedicalInsurance());
        existingPayslip.setOtherTaxed(payslipDTO.getOtherTaxed());
        existingPayslip.setTotalDeductions(payslipDTO.getTotalDeductions());
        existingPayslip.setNetSalary(payslipDTO.getNetSalary());
        existingPayslip.setStatus(payslipDTO.getStatus());
        
        Payslip updatedPayslip = payslipRepository.save(existingPayslip);
        log.info("Payslip updated successfully with ID: {}", updatedPayslip.getId());
        
        return payslipMapper.toDTO(updatedPayslip);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PayslipDTO getPayslipById(Long id) {
        Payslip payslip = getPayslipEntityById(id);
        return payslipMapper.toDTO(payslip);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getAllPayslips() {
        List<Payslip> payslips = payslipRepository.findAll();
        return payslipMapper.toDTOList(payslips);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayslipDTO> getAllPayslips(Pageable pageable) {
        Page<Payslip> payslips = payslipRepository.findAll(pageable);
        return payslips.map(payslipMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getPayslipsByEmployeeId(Long employeeId) {
        List<Payslip> payslips = payslipRepository.findByEmployeeId(employeeId);
        return payslipMapper.toDTOList(payslips);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayslipDTO> getPayslipsByEmployeeId(Long employeeId, Pageable pageable) {
        Page<Payslip> payslips = payslipRepository.findByEmployeeId(employeeId, pageable);
        return payslips.map(payslipMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getPayslipsByStatus(PayslipStatus status) {
        List<Payslip> payslips = payslipRepository.findByStatus(status);
        return payslipMapper.toDTOList(payslips);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayslipDTO> getPayslipsByStatus(PayslipStatus status, Pageable pageable) {
        Page<Payslip> payslips = payslipRepository.findByStatus(status, pageable);
        return payslips.map(payslipMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getPayslipsByPeriod(Integer month, Integer year) {
        List<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year);
        return payslipMapper.toDTOList(payslips);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayslipDTO> getPayslipsByPeriod(Integer month, Integer year, Pageable pageable) {
        Page<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year, pageable);
        return payslips.map(payslipMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayslipDTO> searchPayslips(String keyword, Pageable pageable) {
        Page<Payslip> payslips = payslipRepository.searchPayslips(keyword, pageable);
        return payslips.map(payslipMapper::toDTO);
    }
    
    @Override
    public PayslipDTO approvePayslip(Long id) {
        log.info("Approving payslip with ID: {}", id);
        Payslip payslip = getPayslipEntityById(id);
        
        if (payslip.getStatus() != PayslipStatus.PENDING) {
            throw new BusinessException("Only pending payslips can be approved");
        }
        
        payslip.setStatus(PayslipStatus.APPROVED);
        payslip.setApprovedAt(LocalDateTime.now());
        Payslip updatedPayslip = payslipRepository.save(payslip);
        
        return payslipMapper.toDTO(updatedPayslip);
    }
    
    @Override
    public PayslipDTO rejectPayslip(Long id) {
        log.info("Rejecting payslip with ID: {}", id);
        Payslip payslip = getPayslipEntityById(id);
        
        if (payslip.getStatus() != PayslipStatus.PENDING) {
            throw new BusinessException("Only pending payslips can be rejected");
        }
        
        payslip.setStatus(PayslipStatus.REJECTED);
        Payslip updatedPayslip = payslipRepository.save(payslip);
        
        return payslipMapper.toDTO(updatedPayslip);
    }
    
    @Override
    public PayslipDTO markAsPaid(Long id) {
        log.info("Marking payslip as paid with ID: {}", id);
        Payslip payslip = getPayslipEntityById(id);
        
        if (payslip.getStatus() != PayslipStatus.APPROVED) {
            throw new BusinessException("Only approved payslips can be marked as paid");
        }
        
        payslip.setStatus(PayslipStatus.PAID);
        Payslip updatedPayslip = payslipRepository.save(payslip);
        
        return payslipMapper.toDTO(updatedPayslip);
    }
    
    @Override
    public List<PayslipDTO> generatePayroll(PayrollGenerationDto payrollGenerationDto) {
        log.info("Generating payroll for period {}/{}", payrollGenerationDto.getMonth(), payrollGenerationDto.getYear());
        
        List<Employee> employees;
        
        if (payrollGenerationDto.getEmployeeIds() != null && !payrollGenerationDto.getEmployeeIds().isEmpty()) {
            // Generate for specific employees
            employees = payrollGenerationDto.getEmployeeIds().stream()
                    .map(employeeService::getEmployeeEntityById)
                    .collect(Collectors.toList());
        } else {
            // Generate for all active employees with active employment
            employees = employeeService.getActiveEmployeesWithActiveEmployment().stream()
                    .map(dto -> employeeService.getEmployeeEntityById(dto.getId()))
                    .collect(Collectors.toList());
        }
        
        List<PayslipDTO> generatedPayslips = new ArrayList<>();
        
        for (Employee employee : employees) {
            try {
                // Check if payslip already exists
                if (!payrollGenerationDto.getForceRegenerate() && 
                    existsByEmployeeAndPeriod(employee.getId(), payrollGenerationDto.getMonth(), payrollGenerationDto.getYear())) {
                    log.warn("Payslip already exists for employee {} for period {}/{}", 
                            employee.getCode(), payrollGenerationDto.getMonth(), payrollGenerationDto.getYear());
                    continue;
                }
                
                PayslipDTO payslipDTO = generatePayslipForEmployee(employee.getId(), 
                        payrollGenerationDto.getMonth(), payrollGenerationDto.getYear());
                generatedPayslips.add(payslipDTO);
                
            } catch (Exception e) {
                log.error("Error generating payslip for employee {}: {}", employee.getCode(), e.getMessage());
                // Continue with other employees
            }
        }
        
        log.info("Generated {} payslips for period {}/{}", 
                generatedPayslips.size(), payrollGenerationDto.getMonth(), payrollGenerationDto.getYear());
        
        return generatedPayslips;
    }
    
    @Override
    public PayslipDTO generatePayslipForEmployee(Long employeeId, Integer month, Integer year) {
        log.info("Generating payslip for employee ID: {} for period {}/{}", employeeId, month, year);
        
        Employee employee = employeeService.getEmployeeEntityById(employeeId);
        EmploymentDetails employmentDetails = employmentDetailsService.getActiveEmploymentEntityByEmployeeId(employeeId);
        
        // Calculate salary components
        BigDecimal baseSalary = employmentDetails.getBaseSalary();
        BigDecimal houseAmount = baseSalary.multiply(HOUSE_ALLOWANCE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal transportAmount = baseSalary.multiply(TRANSPORT_ALLOWANCE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal grossSalary = baseSalary.add(houseAmount).add(transportAmount);
        
        // Calculate deductions
        BigDecimal pensionAmount = baseSalary.multiply(PENSION_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal medicalInsurance = baseSalary.multiply(MEDICAL_INSURANCE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal employeeTaxed = calculateIncomeTax(grossSalary);
        BigDecimal otherTaxed = BigDecimal.ZERO; // Can be customized
        
        BigDecimal totalDeductions = pensionAmount.add(medicalInsurance).add(employeeTaxed).add(otherTaxed);
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);
        
        // Create payslip
        Payslip payslip = Payslip.builder()
                .employee(employee)
                .baseSalary(baseSalary)
                .houseAmount(houseAmount)
                .transportAmount(transportAmount)
                .grossSalary(grossSalary)
                .employeeTaxed(employeeTaxed)
                .pensionAmount(pensionAmount)
                .medicalInsurance(medicalInsurance)
                .otherTaxed(otherTaxed)
                .totalDeductions(totalDeductions)
                .netSalary(netSalary)
                .month(month)
                .year(year)
                .status(PayslipStatus.PENDING)
                .build();
        
        // Delete existing payslip if regenerating
        payslipRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .ifPresent(payslipRepository::delete);
        
        Payslip savedPayslip = payslipRepository.save(payslip);
        log.info("Payslip generated successfully with ID: {}", savedPayslip.getId());
        
        return payslipMapper.toDTO(savedPayslip);
    }
    
    @Override
    public void deletePayslip(Long id) {
        log.info("Deleting payslip with ID: {}", id);
        Payslip payslip = getPayslipEntityById(id);
        
        if (payslip.getStatus() == PayslipStatus.PAID) {
            throw new BusinessException("Cannot delete paid payslip");
        }
        
        payslipRepository.delete(payslip);
        log.info("Payslip deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmployeeAndPeriod(Long employeeId, Integer month, Integer year) {
        return payslipRepository.existsByEmployeeIdAndMonthAndYear(employeeId, month, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(PayslipStatus status) {
        return payslipRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByPeriod(Integer month, Integer year) {
        return payslipRepository.countByMonthAndYear(month, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalGrossSalaryByPeriod(Integer month, Integer year) {
        return payslipRepository.sumGrossSalaryByPeriod(month, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalNetSalaryByPeriod(Integer month, Integer year) {
        return payslipRepository.sumNetSalaryByPeriod(month, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalDeductionsByPeriod(Integer month, Integer year) {
        return payslipRepository.sumTotalDeductionsByPeriod(month, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Payslip getPayslipEntityById(Long id) {
        return payslipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip", "id", id));
    }
    
    private BigDecimal calculateIncomeTax(BigDecimal grossSalary) {
        // Rwanda income tax calculation (simplified)
        // This should be implemented based on current tax brackets
        BigDecimal taxableAmount = grossSalary.subtract(new BigDecimal("30000")); // Basic exemption
        
        if (taxableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal tax = BigDecimal.ZERO;
        
        // Tax brackets (simplified - should be configurable)
        if (taxableAmount.compareTo(new BigDecimal("30000")) <= 0) {
            tax = taxableAmount.multiply(new BigDecimal("0.00")); // 0%
        } else if (taxableAmount.compareTo(new BigDecimal("100000")) <= 0) {
            tax = taxableAmount.multiply(new BigDecimal("0.20")); // 20%
        } else {
            tax = taxableAmount.multiply(new BigDecimal("0.30")); // 30%
        }
        
        return tax.setScale(2, RoundingMode.HALF_UP);
    }
}
