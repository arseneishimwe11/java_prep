package rw.arsene.erp.v1.mapper;

import org.springframework.stereotype.Component;
import rw.arsene.erp.v1.dto.EmployeeDTO;
import rw.arsene.erp.v1.dto.SignupRequest;
import rw.arsene.erp.v1.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    
    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        
        return EmployeeDTO.builder()
                .id(employee.getId())
                .code(employee.getCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .mobile(employee.getMobile())
                .dateOfBirth(employee.getDateOfBirth())
                .status(employee.getStatus())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .fullName(employee.getFullName())
                .active(employee.isActive())
                .build();
    }
    
    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Employee.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .mobile(dto.getMobile())
                .dateOfBirth(dto.getDateOfBirth())
                .status(dto.getStatus())
                .build();
    }
    
    public Employee toEntity(SignupRequest signupRequest) {
        if (signupRequest == null) {
            return null;
        }
        
        return Employee.builder()
                .code(signupRequest.getCode())
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .email(signupRequest.getEmail())
                .mobile(signupRequest.getMobile())
                .dateOfBirth(signupRequest.getDateOfBirth())
                .status(signupRequest.getStatus())
                .build();
    }
    
    public List<EmployeeDTO> toDTOList(List<Employee> employees) {
        return employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Employee> toEntityList(List<EmployeeDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}