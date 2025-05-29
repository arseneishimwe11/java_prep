package rw.arsene.erp.v1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.dto.EmployeeDTO;
import rw.arsene.erp.v1.dto.SignupRequest;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.enums.EmployeeStatus;
import rw.arsene.erp.v1.exception.DuplicateResourceException;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.mapper.EmployeeMapper;
import rw.arsene.erp.v1.repository.EmployeeRepository;
import rw.arsene.erp.v1.service.EmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public EmployeeDTO createEmployee(SignupRequest signupRequest) {
        log.info("Creating new employee with email: {}", signupRequest.getEmail());
        
        // Check for duplicates
        if (employeeRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", signupRequest.getEmail());
        }
        
        if (employeeRepository.existsByCode(signupRequest.getCode())) {
            throw new DuplicateResourceException("Employee", "code", signupRequest.getCode());
        }
        
        if (employeeRepository.existsByMobile(signupRequest.getMobile())) {
            throw new DuplicateResourceException("Employee", "mobile", signupRequest.getMobile());
        }
        
        Employee employee = employeeMapper.toEntity(signupRequest);
        employee.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        
        return employeeMapper.toDTO(savedEmployee);
    }
    
    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);
        
        Employee existingEmployee = getEmployeeEntityById(id);
        
        // Check for duplicates (excluding current employee)
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail()) && 
            employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", employeeDTO.getEmail());
        }
        
        if (!existingEmployee.getCode().equals(employeeDTO.getCode()) && 
            employeeRepository.existsByCode(employeeDTO.getCode())) {
            throw new DuplicateResourceException("Employee", "code", employeeDTO.getCode());
        }
        
        if (!existingEmployee.getMobile().equals(employeeDTO.getMobile()) && 
            employeeRepository.existsByMobile(employeeDTO.getMobile())) {
            throw new DuplicateResourceException("Employee", "mobile", employeeDTO.getMobile());
        }
        
        // Update fields
        existingEmployee.setCode(employeeDTO.getCode());
        existingEmployee.setFirstName(employeeDTO.getFirstName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setMobile(employeeDTO.getMobile());
        existingEmployee.setDateOfBirth(employeeDTO.getDateOfBirth());
        existingEmployee.setRoles(employeeDTO.getRoles());
        existingEmployee.setStatus(employeeDTO.getStatus());
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());
        
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = getEmployeeEntityById(id);
        return employeeMapper.toDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByCode(String code) {
        Employee employee = employeeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "code", code));
        return employeeMapper.toDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = getEmployeeEntityByEmail(email);
        return employeeMapper.toDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDTOList(employees);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employees.map(employeeMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status) {
        List<Employee> employees = employeeRepository.findByStatus(status);
        return employeeMapper.toDTOList(employees);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status, Pageable pageable) {
        Page<Employee> employees = employeeRepository.findByStatus(status, pageable);
        return employees.map(employeeMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> searchEmployees(String keyword, Pageable pageable) {
        Page<Employee> employees = employeeRepository.searchEmployees(keyword, pageable);
        return employees.map(employeeMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getActiveEmployeesWithActiveEmployment() {
        List<Employee> employees = employeeRepository.findActiveEmployeesWithActiveEmployment();
        return employeeMapper.toDTOList(employees);
    }
    
    @Override
    public EmployeeDTO activateEmployee(Long id) {
        log.info("Activating employee with ID: {}", id);
        Employee employee = getEmployeeEntityById(id);
        employee.setStatus(EmployeeStatus.ACTIVE);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    @Override
    public EmployeeDTO deactivateEmployee(Long id) {
        log.info("Deactivating employee with ID: {}", id);
        Employee employee = getEmployeeEntityById(id);
        employee.setStatus(EmployeeStatus.INACTIVE);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    @Override
    public EmployeeDTO suspendEmployee(Long id) {
        log.info("Suspending employee with ID: {}", id);
        Employee employee = getEmployeeEntityById(id);
        employee.setStatus(EmployeeStatus.SUSPENDED);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);
        Employee employee = getEmployeeEntityById(id);
        
        // Check if employee has any dependencies (employment details, payslips, etc.)
        // This should be handled based on business rules
        
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return employeeRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByMobile(String mobile) {
        return employeeRepository.existsByMobile(mobile);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(EmployeeStatus status) {
        return employeeRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeEntityByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));
    }
}
