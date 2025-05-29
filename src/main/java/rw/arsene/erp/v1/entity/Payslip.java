package rw.arsene.erp.v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import rw.arsene.erp.v1.enums.PayslipStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payslips",
       uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "month", "year"}),
       indexes = {
           @Index(name = "idx_payslip_month_year", columnList = "month, year"),
           @Index(name = "idx_payslip_status", columnList = "status")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payslip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    // Salary Components
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal baseSalary;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal houseAmount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal transportAmount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal grossSalary;
    
    // Deductions
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal employeeTaxed;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal pensionAmount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal medicalInsurance;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal otherTaxed;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDeductions;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal netSalary;
    
    // Period
    @Column(nullable = false)
    private Integer month;
    
    @Column(nullable = false)
    private Integer year;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PayslipStatus status = PayslipStatus.PENDING;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime approvedAt;
    
    // Helper methods
    public String getPeriodString() {
        return String.format("%02d/%d", month, year);
    }
    
    public boolean isPending() {
        return status == PayslipStatus.PENDING;
    }
    
    public boolean isApproved() {
        return status == PayslipStatus.APPROVED || status == PayslipStatus.PAID;
    }
    
    public void approve() {
        this.status = PayslipStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }
    
    public void markAsPaid() {
        this.status = PayslipStatus.PAID;
        if (this.approvedAt == null) {
            this.approvedAt = LocalDateTime.now();
        }
    }
}
