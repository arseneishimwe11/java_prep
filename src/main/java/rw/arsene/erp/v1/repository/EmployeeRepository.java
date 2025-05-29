package rw.arsene.erp.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.enums.EmployeeStatus;
import rw.arsene.erp.v1.enums.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByCode(String code);
    Optional<Employee> findByMobile(String mobile);
    
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByMobile(String mobile);
    
    List<Employee> findByStatus(EmployeeStatus status);
    List<Employee> findByRoles(Role role);
    
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);
    Page<Employee> findByRoles(Role role, Pageable pageable);
    
    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' AND EXISTS " +
           "(SELECT ed FROM EmploymentDetails ed WHERE ed.employee = e AND ed.status = 'ACTIVE')")
    List<Employee> findActiveEmployeesWithActiveEmployment();
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    long countByStatus(@Param("status") EmployeeStatus status);
}
