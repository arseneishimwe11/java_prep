package rw.arsene.erp.v1.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.EmploymentDetails;
import rw.arsene.erp.v1.entity.Payslip;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.repository.EmployeeRepository;
import rw.arsene.erp.v1.repository.EmploymentDetailsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PayslipMapper {
    
    private final EmployeeRepository employeeRepository;
    private final EmploymentDetailsRepository employmentDetailsRepository;
    
    public PayslipDTO toDTO(Payslip payslip) {
        if (payslip == null) {
            return null;
        }
        
        Employee employee = payslip.getEmployee();
        EmploymentDetails employmentDetails = employmentDetailsRepository
                .findActiveEmploymentByEmployeeId(employee.getId())
                .orElse(null);
        
        return PayslipDTO.builder()
                .id(payslip.getId())
                .employeeId(employee.getId())
                .employeeCode(employee.getCode())
                .employeeName(employee.getFullName())
                .employeeEmail(employee.getEmail())
                .department(employmentDetails != null ? employmentDetails.getDepartment() : "N/A")
                .position(employmentDetails != null ? employmentDetails.getPosition() : "N/A")
                .baseSalary(payslip.getBaseSalary())
                .houseAmount(payslip.getHouseAmount())
                .transportAmount(payslip.getTransportAmount())
                .grossSalary(payslip.getGrossSalary())
                .employeeTaxed(payslip.getEmployeeTaxed())
                .pensionAmount(payslip.getPensionAmount())
                .medicalInsurance(payslip.getMedicalInsurance())
                .otherTaxed(payslip.getOtherTaxed())
                .totalDeductions(payslip.getTotalDeductions())
                .netSalary(payslip.getNetSalary())
                .month(payslip.getMonth())
                .year(payslip.getYear())
                .status(payslip.getStatus())
                .createdAt(payslip.getCreatedAt())
                .updatedAt(payslip.getUpdatedAt())
                .approvedAt(payslip.getApprovedAt())
                .periodString(payslip.getPeriodString())
                .pending(payslip.isPending())
                .approved(payslip.isApproved())
                .build();
    }
    
    public Payslip toEntity(PayslipDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getEmployeeId()));
        
        return Payslip.builder()
                .id(dto.getId())
                .employee(employee)
                .baseSalary(dto.getBaseSalary())
                .houseAmount(dto.getHouseAmount())
                .transportAmount(dto.getTransportAmount())
                .grossSalary(dto.getGrossSalary())
                .employeeTaxed(dto.getEmployeeTaxed())
                .pensionAmount(dto.getPensionAmount())
                .medicalInsurance(dto.getMedicalInsurance())
                .otherTaxed(dto.getOtherTaxed())
                .totalDeductions(dto.getTotalDeductions())
                .netSalary(dto.getNetSalary())
                .month(dto.getMonth())
                .year(dto.getYear())
                .status(dto.getStatus())
                .approvedAt(dto.getApprovedAt())
                .build();
    }
    
    public List<PayslipDTO> toDTOList(List<Payslip> payslips) {
        return payslips.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Payslip> toEntityList(List<PayslipDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}