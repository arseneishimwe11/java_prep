package rw.arsene.erp.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeductionDTO {
    
    private Long id;
    
    @NotBlank(message = "Deduction code is required")
    @Size(max = 20, message = "Deduction code must not exceed 20 characters")
    private String code;
    
    @NotBlank(message = "Deduction name is required")
    @Size(max = 100, message = "Deduction name must not exceed 100 characters")
    private String deductionName;
    
    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Percentage must be greater than or equal to 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Percentage must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Percentage format is invalid")
    private BigDecimal percentage;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotNull(message = "Active status is required")
    private Boolean isActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
