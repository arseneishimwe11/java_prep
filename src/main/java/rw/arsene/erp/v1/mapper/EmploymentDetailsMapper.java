package rw.arsene.erp.v1.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rw.arsene.erp.v1.dto.EmploymentDetailsDTO;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.EmploymentDetails;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmploymentDetailsMapper {
    
    private final EmployeeRepository employeeRepository;
    
    public EmploymentDetailsDTO toDTO(EmploymentDetails employmentDetails) {
        if (employmentDetails == null) {
            return null;
        }
        
        Employee employee = employmentDetails.getEmployee();
        
        return EmploymentDetailsDTO.builder()
                .id(employmentDetails.getId())
                .code(employmentDetails.getCode())
                .employeeId(employee.getId())
                .department(employmentDetails.getDepartment())
                .position(employmentDetails.getPosition())
                .baseSalary(employmentDetails.getBaseSalary())
                .status(employmentDetails.getStatus())
                .joiningDate(employmentDetails.getJoiningDate())
                .terminationDate(employmentDetails.getTerminationDate())
                .notes(employmentDetails.getNotes())
                .createdAt(employmentDetails.getCreatedAt())
                .updatedAt(employmentDetails.getUpdatedAt())
                .build();
    }
    
    public EmploymentDetails toEntity(EmploymentDetailsDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getEmployeeId()));
        
        return EmploymentDetails.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .employee(employee)
                .department(dto.getDepartment())
                .position(dto.getPosition())
                .baseSalary(dto.getBaseSalary())
                .status(dto.getStatus())
                .joiningDate(dto.getJoiningDate())
                .terminationDate(dto.getTerminationDate())
                .notes(dto.getNotes())
                .build();
    }
    
    public List<EmploymentDetailsDTO> toDTOList(List<EmploymentDetails> employmentDetailsList) {
        return employmentDetailsList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<EmploymentDetails> toEntityList(List<EmploymentDetailsDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}