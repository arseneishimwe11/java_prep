package rw.arsene.erp.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.Deduction;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    
    Optional<Deduction> findByCode(String code);
    
    boolean existsByCode(String code);
    
    boolean existsByDeductionName(String deductionName);
    
    List<Deduction> findByIsActiveTrue();
    
    Page<Deduction> findByIsActiveTrue(Pageable pageable);
    
    long countByIsActiveTrue();
    
    @Query("SELECT d FROM Deduction d WHERE " +
           "LOWER(d.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.deductionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Deduction> searchDeductions(@Param("keyword") String keyword, Pageable pageable);
}
