package rw.arsene.erp.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.arsene.erp.v1.enums.EmploymentStatus;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employment Details Data Transfer Object")
public class EmploymentDetailsDTO {
    
    @Schema(description = "Employment details ID", example = "1")
    private Long id;
    
    @NotBlank(message = "Employment code is required")
    @Size(max = 20, message = "Employment code must not exceed 20 characters")
    @Schema(description = "Employment code", example = "EMP001", required = true)
    private String code;
    
    @NotNull(message = "Employee ID is required")
    @Schema(description = "Employee ID", example = "1", required = true)
    private Long employeeId;
    
    @Schema(description = "Employee details")
    private EmployeeDTO employee;
    
    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    @Schema(description = "Department", example = "Information Technology", required = true)
    private String department;
    
    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position must not exceed 100 characters")
    @Schema(description = "Position", example = "Software Developer", required = true)
    private String position;
    
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base salary must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Base salary must have at most 10 integer digits and 2 fraction digits")
    @Schema(description = "Base salary", example = "1000000.00", required = true)
    private BigDecimal baseSalary;
    
    @NotNull(message = "Employment status is required")
    @Schema(description = "Employment status", example = "ACTIVE", required = true)
    private EmploymentStatus status;
    
    @NotNull(message = "Joining date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Joining date", example = "2024-01-15", required = true)
    private Date joiningDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Termination date", example = "2024-12-31")
    private Date terminationDate;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Schema(description = "Additional notes", example = "Probation period completed successfully")
    private String notes;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:00")
    private Date createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:00")
    private Date updatedAt;
}
