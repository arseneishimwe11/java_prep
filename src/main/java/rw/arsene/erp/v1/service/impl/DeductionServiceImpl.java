package rw.arsene.erp.v1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.dto.DeductionDTO;
import rw.arsene.erp.v1.entity.Deduction;
import rw.arsene.erp.v1.exception.DuplicateResourceException;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.mapper.DeductionMapper;
import rw.arsene.erp.v1.repository.DeductionRepository;
import rw.arsene.erp.v1.service.DeductionService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeductionServiceImpl implements DeductionService {
    
    private final DeductionRepository deductionRepository;
    private final DeductionMapper deductionMapper;
    
    @Override
    public DeductionDTO createDeduction(DeductionDTO deductionDTO) {
        log.info("Creating new deduction: {}", deductionDTO.getDeductionName());
        
        // Check for duplicates
        if (deductionDTO.getCode() != null && deductionRepository.existsByCode(deductionDTO.getCode())) {
            throw new DuplicateResourceException("Deduction", "code", deductionDTO.getCode());
        }
        
        if (deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new DuplicateResourceException("Deduction", "deductionName", deductionDTO.getDeductionName());
        }
        
        // Generate code if not provided
        if (deductionDTO.getCode() == null || deductionDTO.getCode().trim().isEmpty()) {
            deductionDTO.setCode(generateDeductionCode());
        }
        
        Deduction deduction = deductionMapper.toEntity(deductionDTO);
        Deduction savedDeduction = deductionRepository.save(deduction);
        
        log.info("Deduction created successfully with ID: {}", savedDeduction.getId());
        return deductionMapper.toDTO(savedDeduction);
    }
    
    @Override
    public DeductionDTO updateDeduction(Long id, DeductionDTO deductionDTO) {
        log.info("Updating deduction with ID: {}", id);
        
        Deduction existingDeduction = getDeductionEntityById(id);
        
        // Check for duplicates (excluding current record)
        if (!existingDeduction.getCode().equals(deductionDTO.getCode()) && 
            deductionRepository.existsByCode(deductionDTO.getCode())) {
            throw new DuplicateResourceException("Deduction", "code", deductionDTO.getCode());
        }
        
        if (!existingDeduction.getDeductionName().equals(deductionDTO.getDeductionName()) && 
            deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new DuplicateResourceException("Deduction", "deductionName", deductionDTO.getDeductionName());
        }
        
        // Update fields
        existingDeduction.setCode(deductionDTO.getCode());
        existingDeduction.setDeductionName(deductionDTO.getDeductionName());
        existingDeduction.setPercentage(deductionDTO.getPercentage());
        existingDeduction.setDescription(deductionDTO.getDescription());
        existingDeduction.setIsActive(deductionDTO.getIsActive());
        
        Deduction updatedDeduction = deductionRepository.save(existingDeduction);
        log.info("Deduction updated successfully with ID: {}", updatedDeduction.getId());
        
        return deductionMapper.toDTO(updatedDeduction);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DeductionDTO getDeductionById(Long id) {
        Deduction deduction = getDeductionEntityById(id);
        return deductionMapper.toDTO(deduction);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DeductionDTO getDeductionByCode(String code) {
        Deduction deduction = deductionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction", "code", code));
        return deductionMapper.toDTO(deduction);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DeductionDTO> getAllDeductions() {
        List<Deduction> deductions = deductionRepository.findAll();
        return deductionMapper.toDTOList(deductions);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DeductionDTO> getAllDeductions(Pageable pageable) {
        Page<Deduction> deductions = deductionRepository.findAll(pageable);
        return deductions.map(deductionMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DeductionDTO> getActiveDeductions() {
        List<Deduction> deductions = deductionRepository.findByIsActiveTrue();
        return deductionMapper.toDTOList(deductions);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DeductionDTO> getActiveDeductions(Pageable pageable) {
        Page<Deduction> deductions = deductionRepository.findByIsActiveTrue(pageable);
        return deductions.map(deductionMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DeductionDTO> searchDeductions(String keyword, Pageable pageable) {
        Page<Deduction> deductions = deductionRepository.searchDeductions(keyword, pageable);
        return deductions.map(deductionMapper::toDTO);
    }
    
    @Override
    public DeductionDTO activateDeduction(Long id) {
        log.info("Activating deduction with ID: {}", id);
        Deduction deduction = getDeductionEntityById(id);
        deduction.setIsActive(true);
        Deduction updatedDeduction = deductionRepository.save(deduction);
        return deductionMapper.toDTO(updatedDeduction);
    }
    
    @Override
    public DeductionDTO deactivateDeduction(Long id) {
        log.info("Deactivating deduction with ID: {}", id);
        Deduction deduction = getDeductionEntityById(id);
        deduction.setIsActive(false);
        Deduction updatedDeduction = deductionRepository.save(deduction);
        return deductionMapper.toDTO(updatedDeduction);
    }
    
    @Override
    public void deleteDeduction(Long id) {
        log.info("Deleting deduction with ID: {}", id);
        Deduction deduction = getDeductionEntityById(id);
        
        // Check if deduction is being used in any payslips
        // This should be handled based on business rules
        
        deductionRepository.delete(deduction);
        log.info("Deduction deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return deductionRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDeductionName(String deductionName) {
        return deductionRepository.existsByDeductionName(deductionName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveDeductions() {
        return deductionRepository.countByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Deduction getDeductionEntityById(Long id) {
        return deductionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Deduction> getActiveDeductionEntities() {
        return deductionRepository.findByIsActiveTrue();
    }
    
    private String generateDeductionCode() {
        String prefix = "DED";
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + "-" + suffix;
    }
}
