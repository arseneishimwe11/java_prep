package rw.arsene.erp.v1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.arsene.erp.v1.dto.EmploymentDetailsDTO;
import rw.arsene.erp.v1.enums.EmploymentStatus;

import java.util.List;

public interface EmploymentDetailsService {
    
    EmploymentDetailsDTO createEmploymentDetails(EmploymentDetailsDTO employmentDetailsDTO);
    
    EmploymentDetailsDTO updateEmploymentDetails(Long id, EmploymentDetailsDTO employmentDetailsDTO);
    
    EmploymentDetailsDTO getEmploymentDetailsById(Long id);
    
    EmploymentDetailsDTO getActiveEmploymentByEmployeeId(Long employeeId);
    
    Page<EmploymentDetailsDTO> getAllEmploymentDetails(Pageable pageable);
    
    List<EmploymentDetailsDTO> getEmploymentDetailsByEmployeeId(Long employeeId);
    
    Page<EmploymentDetailsDTO> getEmploymentDetailsByStatus(EmploymentStatus status, Pageable pageable);
    
    List<EmploymentDetailsDTO> getEmploymentDetailsByDepartment(String department);
    
    Page<EmploymentDetailsDTO> searchEmploymentDetails(String keyword, Pageable pageable);
    
    EmploymentDetailsDTO activateEmployment(Long id);
    
    EmploymentDetailsDTO terminateEmployment(Long id);
    
    EmploymentDetailsDTO suspendEmployment(Long id);
    
    void deleteEmploymentDetails(Long id);
    
    boolean existsByCode(String code);
    
    boolean hasActiveEmployment(Long employeeId);
    
    long countByStatus(EmploymentStatus status);
    
    long countByDepartment(String department);
}
