package rw.arsene.erp.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.Payslip;
import rw.arsene.erp.v1.enums.PayslipStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    
    List<Payslip> findByEmployeeIdOrderByYearDescMonthDesc(Long employeeId);
    Page<Payslip> findByEmployeeIdOrderByYearDescMonthDesc(Long employeeId, Pageable pageable);
    
    List<Payslip> findByStatus(PayslipStatus status);
    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    List<Payslip> findByMonthAndYearAndStatus(Integer month, Integer year, PayslipStatus status);
    
    Page<Payslip> findAllByOrderByYearDescMonthDescCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT p FROM Payslip p WHERE p.employee.id = :employeeId AND p.month = :month AND p.year = :year")
    Optional<Payslip> findByEmployeeIdAndMonthAndYear(@Param("employeeId") Long employeeId, 
                                                     @Param("month") Integer month, 
                                                     @Param("year") Integer year);
    
    @Query("SELECT COUNT(p) > 0 FROM Payslip p WHERE p.employee.id = :employeeId AND p.month = :month AND p.year = :year")
    boolean existsByEmployeeIdAndMonthAndYear(@Param("employeeId") Long employeeId, 
                                            @Param("month") Integer month, 
                                            @Param("year") Integer year);
    
    @Query("SELECT p FROM Payslip p WHERE p.month = :month AND p.year = :year AND p.status = 'PENDING'")
    List<Payslip> findPendingPayslipsByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
    
    @Query("SELECT COUNT(p) FROM Payslip p WHERE p.status = :status")
    long countByStatus(@Param("status") PayslipStatus status);
    
    @Query("SELECT DISTINCT p.year FROM Payslip p ORDER BY p.year DESC")
    List<Integer> findDistinctYears();
    
    @Query("SELECT DISTINCT p.month FROM Payslip p WHERE p.year = :year ORDER BY p.month DESC")
    List<Integer> findDistinctMonthsByYear(@Param("year") Integer year);
}
