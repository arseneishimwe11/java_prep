package rw.arsene.erp.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.arsene.erp.v1.enums.MessageSentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    
    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    // Employee details for display
    private String employeeCode;
    private String employeeName;
    private String employeeEmail;
    
    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;
    
    @NotBlank(message = "Month/Year is required")
    @Size(max = 20, message = "Month/Year must not exceed 20 characters")
    private String monthYear;
    
    @NotNull(message = "Status is required")
    private MessageSentStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentAt;
    
    private String errorMessage;
    
    // Computed fields
    private boolean sent;
    private boolean failed;
    
    public boolean isSent() {
        return status == MessageSentStatus.SENT || status == MessageSentStatus.DELIVERED;
    }
    
    public boolean isFailed() {
        return status == MessageSentStatus.FAILED;
    }
}
