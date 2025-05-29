package rw.arsene.erp.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    
    private String message;
    private boolean success;
    private Object data;
    
    public static MessageResponse success(String message) {
        return MessageResponse.builder()
                .message(message)
                .success(true)
                .build();
    }
    
    public static MessageResponse success(String message, Object data) {
        return MessageResponse.builder()
                .message(message)
                .success(true)
                .data(data)
                .build();
    }
    
    public static MessageResponse error(String message) {
        return MessageResponse.builder()
                .message(message)
                .success(false)
                .build();
    }
}
