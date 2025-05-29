package rw.arsene.erp.v1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.arsene.erp.v1.dto.MessageDTO;
import rw.arsene.erp.v1.entity.Message;
import java.util.List;

public interface MessageService {
    
    MessageDTO createMessage(MessageDTO messageDTO);
    MessageDTO updateMessage(Long id, MessageDTO messageDTO);
    MessageDTO getMessageById(Long id);
    
    List<MessageDTO> getAllMessages();
    Page<MessageDTO> getAllMessages(Pageable pageable);
    List<MessageDTO> getMessagesByEmployeeId(Long employeeId);
    Page<MessageDTO> getMessagesByEmployeeId(Long employeeId, Pageable pageable);
    List<MessageDTO> getMessagesByStatus(MessageStatus status);
    Page<MessageDTO> getMessagesByStatus(MessageStatus status, Pageable pageable);
    List<MessageDTO> getMessagesByPeriod(String monthYear);
    Page<MessageDTO> getMessagesByPeriod(String monthYear, Pageable pageable);
    
    Page<MessageDTO> searchMessages(String keyword, Pageable pageable);
    
    MessageDTO sendMessage(Long id);
    MessageDTO markAsFailed(Long id, String errorMessage);
    MessageDTO retrySendMessage(Long id);
    
    List<MessageDTO> sendPayslipNotifications(Integer month, Integer year);
    MessageDTO sendPayslipNotificationToEmployee(Long employeeId, Integer month, Integer year);
    
    void deleteMessage(Long id);
    
    long countByStatus(MessageStatus status);
    long countByPeriod(String monthYear);
    
    // Internal methods for other services
    Message getMessageEntityById(Long id);
}