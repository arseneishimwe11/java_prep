package rw.arsene.erp.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.Deduction;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    
    Optional<Deduction> findByCode(String code);
    Optional<Deduction> findByDeductionName(String deductionName);
    
    boolean existsByCode(String code);
    boolean existsByDeductionName(String deductionName);
    
    List<Deduction> findByIsActiveTrue();
    List<Deduction> findByIsActiveFalse();
    
    @Query("SELECT d FROM Deduction d WHERE d.isActive = true ORDER BY d.deductionName")
    List<Deduction> findAllActiveDeductionsOrdered();
    
    @Query("SELECT d FROM Deduction d WHERE d.deductionName IN ('Employee Tax', 'Pension', 'Medical Insurance', 'Others') AND d.isActive = true")
    List<Deduction> findStandardDeductions();
    
    @Query("SELECT d FROM Deduction d WHERE d.deductionName IN ('Housing', 'Transport') AND d.isActive = true")
    List<Deduction> findAllowanceDeductions();
}
