package rw.arsene.erp.v1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.dto.EmploymentDetailsDTO;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.EmploymentDetails;
import rw.arsene.erp.v1.enums.EmploymentStatus;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.exception.DuplicateResourceException;
import rw.arsene.erp.v1.mapper.EmploymentDetailsMapper;
import rw.arsene.erp.v1.repository.EmployeeRepository;
import rw.arsene.erp.v1.repository.EmploymentDetailsRepository;
import rw.arsene.erp.v1.service.EmploymentDetailsService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmploymentDetailsServiceImpl implements EmploymentDetailsService {
    
    private final EmploymentDetailsRepository employmentDetailsRepository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentDetailsMapper employmentDetailsMapper;
    
    @Override
    public EmploymentDetailsDTO createEmploymentDetails(EmploymentDetailsDTO employmentDetailsDTO) {
        log.info("Creating employment details for employee ID: {}", employmentDetailsDTO.getEmployeeId());
        
        // Check if employee exists
        Employee employee = employeeRepository.findById(employmentDetailsDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employmentDetailsDTO.getEmployeeId()));
        
        // Check if employee already has active employment
        if (employmentDetailsRepository.existsByEmployeeAndStatus(employee, EmploymentStatus.ACTIVE)) {
            throw new DuplicateResourceException("Employee already has active employment details");
        }
        
        // Check if code already exists
        if (employmentDetailsDTO.getCode() != null && employmentDetailsRepository.existsByCode(employmentDetailsDTO.getCode())) {
            throw new DuplicateResourceException("Employment details with code " + employmentDetailsDTO.getCode() + " already exists");
        }
        
        EmploymentDetails employmentDetails = employmentDetailsMapper.toEntity(employmentDetailsDTO);
        employmentDetails.setEmployee(employee);
        employmentDetails.setCreatedAt(new Date());
        employmentDetails.setUpdatedAt(new Date());
        
        EmploymentDetails savedEmploymentDetails = employmentDetailsRepository.save(employmentDetails);
        log.info("Employment details created successfully with ID: {}", savedEmploymentDetails.getId());
        
        return employmentDetailsMapper.toDTO(savedEmploymentDetails);
    }
    
    @Override
    public EmploymentDetailsDTO updateEmploymentDetails(Long id, EmploymentDetailsDTO employmentDetailsDTO) {
        log.info("Updating employment details with ID: {}", id);
        
        EmploymentDetails existingEmploymentDetails = employmentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with ID: " + id));
        
        // Check if code already exists for another employment details
        if (employmentDetailsDTO.getCode() != null && 
            !employmentDetailsDTO.getCode().equals(existingEmploymentDetails.getCode()) &&
            employmentDetailsRepository.existsByCode(employmentDetailsDTO.getCode())) {
            throw new DuplicateResourceException("Employment details with code " + employmentDetailsDTO.getCode() + " already exists");
        }
        
        // Update fields
        existingEmploymentDetails.setCode(employmentDetailsDTO.getCode());
        existingEmploymentDetails.setDepartment(employmentDetailsDTO.getDepartment());
        existingEmploymentDetails.setPosition(employmentDetailsDTO.getPosition());
        existingEmploymentDetails.setBaseSalary(employmentDetailsDTO.getBaseSalary());
        existingEmploymentDetails.setStatus(employmentDetailsDTO.getStatus());
        existingEmploymentDetails.setJoiningDate(employmentDetailsDTO.getJoiningDate());
        existingEmploymentDetails.setTerminationDate(employmentDetailsDTO.getTerminationDate());
        existingEmploymentDetails.setUpdatedAt(new Date());
        
        EmploymentDetails updatedEmploymentDetails = employmentDetailsRepository.save(existingEmploymentDetails);
        log.info("Employment details updated successfully with ID: {}", updatedEmploymentDetails.getId());
        
        return employmentDetailsMapper.toDTO(updatedEmploymentDetails);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmploymentDetailsDTO getEmploymentDetailsById(Long id) {
        log.info("Fetching employment details with ID: {}", id);
        
        EmploymentDetails employmentDetails = employmentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with ID: " + id));
        
        return employmentDetailsMapper.toDTO(employmentDetails);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmploymentDetailsDTO getActiveEmploymentByEmployeeId(Long employeeId) {
        log.info("Fetching active employment details for employee ID: {}", employeeId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        EmploymentDetails employmentDetails = employmentDetailsRepository.findByEmployeeAndStatus(employee, EmploymentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active employment details not found for employee ID: " + employeeId));
        
        return employmentDetailsMapper.toDTO(employmentDetails);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentDetailsDTO> getAllEmploymentDetails(Pageable pageable) {
        log.info("Fetching all employment details with pagination");
        
        Page<EmploymentDetails> employmentDetailsPage = employmentDetailsRepository.findAll(pageable);
        return employmentDetailsPage.map(employmentDetailsMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmploymentDetailsDTO> getEmploymentDetailsByEmployeeId(Long employeeId) {
        log.info("Fetching employment details for employee ID: {}", employeeId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        List<EmploymentDetails> employmentDetailsList = employmentDetailsRepository.findByEmployee(employee);
        return employmentDetailsList.stream()
                .map(employmentDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentDetailsDTO> getEmploymentDetailsByStatus(EmploymentStatus status, Pageable pageable) {
        log.info("Fetching employment details by status: {}", status);
        
        Page<EmploymentDetails> employmentDetailsPage = employmentDetailsRepository.findByStatus(status, pageable);
        return employmentDetailsPage.map(employmentDetailsMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmploymentDetailsDTO> getEmploymentDetailsByDepartment(String department) {
        log.info("Fetching employment details by department: {}", department);
        
        List<EmploymentDetails> employmentDetailsList = employmentDetailsRepository.findByDepartment(department);
        return employmentDetailsList.stream()
                .map(employmentDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentDetailsDTO> searchEmploymentDetails(String keyword, Pageable pageable) {
        log.info("Searching employment details with keyword: {}", keyword);
        
        Page<EmploymentDetails> employmentDetailsPage = employmentDetailsRepository.searchEmploymentDetails(keyword, pageable);
        return employmentDetailsPage.map(employmentDetailsMapper::toDTO);
    }
    
    @Override
    public EmploymentDetailsDTO activateEmployment(Long id) {
        log.info("Activating employment details with ID: {}", id);
        
        EmploymentDetails employmentDetails = employmentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with ID: " + id));
        
        // Deactivate any existing active employment for the same employee
        employmentDetailsRepository.findByEmployeeAndStatus(employmentDetails.getEmployee(), EmploymentStatus.ACTIVE)
                .ifPresent(activeEmployment -> {
                    activeEmployment.setStatus(EmploymentStatus.TERMINATED);
                    activeEmployment.setTerminationDate(new Date());
                    activeEmployment.setUpdatedAt(new Date());
                    employmentDetailsRepository.save(activeEmployment);
                });
        
        employmentDetails.setStatus(EmploymentStatus.ACTIVE);
        employmentDetails.setJoiningDate(new Date());
        employmentDetails.setTerminationDate(null);
        employmentDetails.setUpdatedAt(new Date());
        
        EmploymentDetails updatedEmploymentDetails = employmentDetailsRepository.save(employmentDetails);
        log.info("Employment details activated successfully with ID: {}", updatedEmploymentDetails.getId());
        
        return employmentDetailsMapper.toDTO(updatedEmploymentDetails);
    }
    
    @Override
    public EmploymentDetailsDTO terminateEmployment(Long id) {
        log.info("Terminating employment details with ID: {}", id);
        
        EmploymentDetails employmentDetails = employmentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with ID: " + id));
        
        employmentDetails.setStatus(EmploymentStatus.TERMINATED);
        employmentDetails.setTerminationDate(new Date());
        employmentDetails.setUpdatedAt(new Date());
        
        EmploymentDetails updatedEmploymentDetails = employmentDetailsRepository.save(employmentDetails);
        log.info("Employment details terminated successfully with ID: {}", updatedEmploymentDetails.getId());
        
        return employmentDetailsMapper.toDTO(updatedEmploymentDetails);
    }
    
    @Override
    public EmploymentDetailsDTO suspendEmployment(Long id) {
        log.info("Suspending employment details with ID: {}", id);
        
        EmploymentDetails employmentDetails = employmentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with ID: " + id));
        
        employmentDetails.setStatus(EmploymentStatus.SUSPENDED);
        employmentDetails.setUpdatedAt(new Date());
        
        EmploymentDetails updatedEmploymentDetails = employmentDetailsRepository.save(employmentDetails);
        log.info("Employment details suspended successfully with ID: {}", updatedEmploymentDetails.getId());
        
        return employmentDetailsMapper.toDTO(updatedEmploymentDetails);
    }
    
    @Override
    public void deleteEmploymentDetails(Long id) {
        log.info("Deleting employment details with ID: {}", id);
        
        EmploymentDetails employmentDetails = employmentDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with ID: " + id));
        
        // Check if employment details can be deleted (e.g., not referenced in payslips)
        // This would require additional business logic based on requirements
        
        employmentDetailsRepository.delete(employmentDetails);
        log.info("Employment details deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return employmentDetailsRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveEmployment(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        return employmentDetailsRepository.existsByEmployeeAndStatus(employee, EmploymentStatus.ACTIVE);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(EmploymentStatus status) {
        return employmentDetailsRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByDepartment(String department) {
        return employmentDetailsRepository.countByDepartment(department);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmploymentDetails getActiveEmploymentEntityByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        return employmentDetailsRepository.findByEmployeeAndStatus(employee, EmploymentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active employment details not found for employee ID: " + employeeId));
    }
}
