package rw.arsene.erp.v1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.arsene.erp.v1.dto.EmployeeDTO;
import rw.arsene.erp.v1.dto.SignupRequest;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.enums.EmployeeStatus;

import java.util.List;

public interface EmployeeService {
    
    EmployeeDTO createEmployee(SignupRequest signupRequest);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO getEmployeeByCode(String code);
    EmployeeDTO getEmployeeByEmail(String email);
    
    List<EmployeeDTO> getAllEmployees();
    Page<EmployeeDTO> getAllEmployees(Pageable pageable);
    List<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status);
    Page<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status, Pageable pageable);
    
    Page<EmployeeDTO> searchEmployees(String keyword, Pageable pageable);
    List<EmployeeDTO> getActiveEmployeesWithActiveEmployment();
    
    EmployeeDTO activateEmployee(Long id);
    EmployeeDTO deactivateEmployee(Long id);
    EmployeeDTO suspendEmployee(Long id);
    
    void deleteEmployee(Long id);
    
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByMobile(String mobile);
    
    long countByStatus(EmployeeStatus status);
    
    // Internal methods for other services
    Employee getEmployeeEntityById(Long id);
    Employee getEmployeeEntityByEmail(String email);
}
