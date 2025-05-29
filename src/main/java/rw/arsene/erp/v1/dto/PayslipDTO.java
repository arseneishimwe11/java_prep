package rw.arsene.erp.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.arsene.erp.v1.enums.PayslipStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayslipDTO {
    
    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    // Employee details for display
    private String employeeCode;
    private String employeeName;
    private String employeeEmail;
    private String department;
    private String position;
    
    // Salary Components
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base salary must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Base salary format is invalid")
    private BigDecimal baseSalary;
    
    @NotNull(message = "House amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "House amount must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "House amount format is invalid")
    private BigDecimal houseAmount;
    
    @NotNull(message = "Transport amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Transport amount must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Transport amount format is invalid")
    private BigDecimal transportAmount;
    
    @NotNull(message = "Gross salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Gross salary must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Gross salary format is invalid")
    private BigDecimal grossSalary;
    
    // Deductions
    @NotNull(message = "Employee tax is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Employee tax must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Employee tax format is invalid")
    private BigDecimal employeeTaxed;
    
    @NotNull(message = "Pension amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Pension amount must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Pension amount format is invalid")
    private BigDecimal pensionAmount;
    
    @NotNull(message = "Medical insurance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Medical insurance must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Medical insurance format is invalid")
    private BigDecimal medicalInsurance;
    
    @NotNull(message = "Other tax is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Other tax must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Other tax format is invalid")
    private BigDecimal otherTaxed;
    
    @NotNull(message = "Total deductions is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total deductions must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Total deductions format is invalid")
    private BigDecimal totalDeductions;
    
    @NotNull(message = "Net salary is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Net salary must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Net salary format is invalid")
    private BigDecimal netSalary;
    
    // Period
    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;
    
    @NotNull(message = "Year is required")
    @Min(value = 2020, message = "Year must be 2020 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer year;
    
    @NotNull(message = "Status is required")
    private PayslipStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    // Computed fields
    private String periodString;
    private boolean pending;
    private boolean approved;
    
    public String getPeriodString() {
        return String.format("%02d/%d", month, year);
    }
    
    public boolean isPending() {
        return status == PayslipStatus.PENDING;
    }
    
    public boolean isApproved() {
        return status == PayslipStatus.APPROVED || status == PayslipStatus.PAID;
    }
}
