package rw.arsene.erp.v1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.arsene.erp.v1.dto.DeductionDTO;
import rw.arsene.erp.v1.entity.Deduction;

import java.util.List;

public interface DeductionService {
    
    DeductionDTO createDeduction(DeductionDTO deductionDTO);
    DeductionDTO updateDeduction(Long id, DeductionDTO deductionDTO);
    DeductionDTO getDeductionById(Long id);
    DeductionDTO getDeductionByCode(String code);
    
    List<DeductionDTO> getAllDeductions();
    Page<DeductionDTO> getAllDeductions(Pageable pageable);
    List<DeductionDTO> getActiveDeductions();
    Page<DeductionDTO> getActiveDeductions(Pageable pageable);
    
    Page<DeductionDTO> searchDeductions(String keyword, Pageable pageable);
    
    DeductionDTO activateDeduction(Long id);
    DeductionDTO deactivateDeduction(Long id);
    
    void deleteDeduction(Long id);
    
    boolean existsByCode(String code);
    boolean existsByDeductionName(String deductionName);
    
    long countActiveDeductions();
    
    // Internal methods for other services
    Deduction getDeductionEntityById(Long id);
    List<Deduction> getActiveDeductionEntities();
}
