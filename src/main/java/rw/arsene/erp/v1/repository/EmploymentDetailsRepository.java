package rw.arsene.erp.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.EmploymentDetails;
import rw.arsene.erp.v1.enums.EmploymentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentDetailsRepository extends JpaRepository<EmploymentDetails, Long> {

 Optional<EmploymentDetails> findByCode(String code);

 boolean existsByCode(String code);

 List<EmploymentDetails> findByEmployee(Employee employee);

 Optional<EmploymentDetails> findByEmployeeAndStatus(Employee employee, EmploymentStatus status);

 boolean existsByEmployeeAndStatus(Employee employee, EmploymentStatus status);

 Page<EmploymentDetails> findByStatus(EmploymentStatus status, Pageable pageable);

 List<EmploymentDetails> findByDepartment(String department);

 Page<EmploymentDetails> findByDepartment(String department, Pageable pageable);

 List<EmploymentDetails> findByPosition(String position);

 Page<EmploymentDetails> findByPosition(String position, Pageable pageable);

 @Query("SELECT ed FROM EmploymentDetails ed WHERE " +
        "LOWER(ed.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(ed.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(ed.position) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(ed.employee.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(ed.employee.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(ed.employee.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
 Page<EmploymentDetails> searchEmploymentDetails(@Param("keyword") String keyword, Pageable pageable);

 long countByStatus(EmploymentStatus status);

 long countByDepartment(String department);

 long countByPosition(String position);

 @Query("SELECT DISTINCT ed.department FROM EmploymentDetails ed ORDER BY ed.department")
 List<String> findAllDepartments();

 @Query("SELECT DISTINCT ed.position FROM EmploymentDetails ed ORDER BY ed.position")
 List<String> findAllPositions();

 @Query("SELECT ed FROM EmploymentDetails ed WHERE ed.status = 'ACTIVE'")
 List<EmploymentDetails> findAllActiveEmployments();

 @Query("SELECT ed FROM EmploymentDetails ed WHERE ed.status = 'ACTIVE'")
 Page<EmploymentDetails> findAllActiveEmployments(Pageable pageable);

 @Query("SELECT ed FROM EmploymentDetails ed WHERE ed.employee.id = :employeeId AND ed.status = 'ACTIVE'")
 Optional<EmploymentDetails> findActiveEmploymentByEmployeeId(@Param("employeeId") Long employeeId);
}
