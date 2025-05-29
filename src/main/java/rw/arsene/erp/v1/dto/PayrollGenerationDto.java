package rw.arsene.erp.v1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollGenerationDto {
    
    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;
    
    @NotNull(message = "Year is required")
    @Min(value = 2020, message = "Year must be 2020 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer year;
    
    // Optional: specific employee IDs to generate payroll for
    private List<Long> employeeIds;
    
    // Optional: specific departments to generate payroll for
    private List<String> departments;
    
    // Optional: force regeneration even if payroll exists
    @Builder.Default
    private Boolean forceRegenerate = false;
    
    public String getPeriodString() {
        return String.format("%02d/%d", month, year);
    }
}
