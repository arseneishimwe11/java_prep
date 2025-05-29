package rw.arsene.erp.v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import rw.arsene.erp.v1.enums.MessageSentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages",
       indexes = {
           @Index(name = "idx_message_employee", columnList = "employee_id"),
           @Index(name = "idx_message_sent_at", columnList = "sentAt"),
           @Index(name = "idx_message_status", columnList = "status")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(nullable = false, length = 20)
    private String monthYear;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MessageSentStatus status = MessageSentStatus.PENDING;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt;
    
    @Column
    private String errorMessage;
    
    // Helper methods
    public boolean isSent() {
        return status == MessageSentStatus.SENT || status == MessageSentStatus.DELIVERED;
    }
    
    public boolean isFailed() {
        return status == MessageSentStatus.FAILED;
    }
    
    public void markAsSent() {
        this.status = MessageSentStatus.SENT;
    }
    
    public void markAsDelivered() {
        this.status = MessageSentStatus.DELIVERED;
    }
    
    public void markAsFailed(String errorMessage) {
        this.status = MessageSentStatus.FAILED;
        this.errorMessage = errorMessage;
    }
}