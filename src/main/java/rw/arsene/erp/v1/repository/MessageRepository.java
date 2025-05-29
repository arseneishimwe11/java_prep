package rw.arsene.erp.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.Message;
import rw.arsene.erp.v1.enums.MessageSentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByEmployeeIdOrderBySentAtDesc(Long employeeId);
    Page<Message> findByEmployeeIdOrderBySentAtDesc(Long employeeId, Pageable pageable);
    
    List<Message> findByStatus(MessageSentStatus status);
    List<Message> findByMonthYear(String monthYear);
    
    Page<Message> findAllByOrderBySentAtDesc(Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE m.status = :status AND m.sentAt >= :since")
    List<Message> findByStatusAndSentAtAfter(@Param("status") MessageSentStatus status, 
                                           @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.status = :status")
    long countByStatus(@Param("status") MessageSentStatus status);
    
    @Query("SELECT m FROM Message m WHERE m.employee.id = :employeeId AND m.monthYear = :monthYear")
    List<Message> findByEmployeeIdAndMonthYear(@Param("employeeId") Long employeeId, 
                                             @Param("monthYear") String monthYear);
    
    @Query("SELECT COUNT(m) > 0 FROM Message m WHERE m.employee.id = :employeeId AND m.monthYear = :monthYear")
    boolean existsByEmployeeIdAndMonthYear(@Param("employeeId") Long employeeId, 
                                         @Param("monthYear") String monthYear);
}
