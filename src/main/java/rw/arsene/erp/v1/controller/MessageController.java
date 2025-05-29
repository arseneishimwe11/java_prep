package rw.arsene.erp.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.arsene.erp.v1.dto.MessageDTO;
import rw.arsene.erp.v1.enums.MessageStatus;
import rw.arsene.erp.v1.service.MessageService;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Tag(name = "Message Management", description = "APIs for managing messages")
public class MessageController {
    
    private final MessageService messageService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Create and send message", description = "Creates and sends a new message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Message created and sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<MessageDTO> createAndSendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO createdMessage = messageService.createAndSendMessage(messageDTO);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Update message", description = "Updates an existing message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<MessageDTO> updateMessage(
            @Parameter(description = "Message ID") @PathVariable Long id,
            @Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO updatedMessage = messageService.updateMessage(id, messageDTO);
        return ResponseEntity.ok(updatedMessage);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get message by ID", description = "Retrieves a message by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message found"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<MessageDTO> getMessageById(
            @Parameter(description = "Message ID") @PathVariable Long id) {
        MessageDTO message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get all messages", description = "Retrieves all messages with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<MessageDTO>> getAllMessages(Pageable pageable) {
        Page<MessageDTO> messages = messageService.getAllMessages(pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get messages by status", description = "Retrieves messages by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<MessageDTO>> getMessagesByStatus(
            @Parameter(description = "Message status") @PathVariable MessageStatus status,
            Pageable pageable) {
        Page<MessageDTO> messages = messageService.getMessagesByStatus(status, pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Search messages", description = "Searches messages by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<MessageDTO>> searchMessages(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            Pageable pageable) {
        Page<MessageDTO> messages = messageService.searchMessages(keyword, pageable);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping("/{id}/send")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Send message", description = "Sends a message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message sent successfully"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "400", description = "Message cannot be sent"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<MessageDTO> sendMessage(
            @Parameter(description = "Message ID") @PathVariable Long id) {
        MessageDTO message = messageService.sendMessage(id);
        return ResponseEntity.ok(message);
    }
    
    @PostMapping("/{id}/retry")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Retry sending message", description = "Retries sending a failed message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message retry initiated successfully"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "400", description = "Message cannot be retried"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<MessageDTO> retryMessage(
            @Parameter(description = "Message ID") @PathVariable Long id) {
        MessageDTO message = messageService.retryMessage(id);
        return ResponseEntity.ok(message);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete message", description = "Deletes a message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Message deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Message ID") @PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Count messages by status", description = "Counts messages by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countByStatus(
            @Parameter(description = "Message status") @PathVariable MessageStatus status) {
        long count = messageService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get failed messages", description = "Retrieves all failed messages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Failed messages retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<MessageDTO>> getFailedMessages(Pageable pageable) {
        Page<MessageDTO> messages = messageService.getFailedMessages(pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Get pending messages", description = "Retrieves all pending messages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending messages retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<MessageDTO>> getPendingMessages(Pageable pageable) {
        Page<MessageDTO> messages = messageService.getPendingMessages(pageable);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Broadcast message", description = "Broadcasts a message to all employees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Broadcast message created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<MessageDTO> broadcastMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO broadcastMessage = messageService.broadcastMessage(messageDTO);
        return new ResponseEntity<>(broadcastMessage, HttpStatus.CREATED);
    }
}
