package rw.arsene.erp.v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deductions",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "code"),
           @UniqueConstraint(columnNames = "deductionName")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deduction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String code;
    
    @Column(unique = true, nullable = false, length = 100)
    private String deductionName;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Helper methods
    public BigDecimal calculateDeduction(BigDecimal baseSalary) {
        if (!isActive || baseSalary == null || percentage == null) {
            return BigDecimal.ZERO;
        }
        return baseSalary.multiply(percentage).divide(BigDecimal.valueOf(100));
    }
}
