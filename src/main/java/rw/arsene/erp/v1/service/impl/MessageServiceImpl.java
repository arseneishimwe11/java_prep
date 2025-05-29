package rw.arsene.erp.v1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.dto.MessageDTO;
import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.Message;
import rw.arsene.erp.v1.exception.BusinessException;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.mapper.MessageMapper;
import rw.arsene.erp.v1.repository.MessageRepository;
import rw.arsene.erp.v1.service.EmployeeService;
import rw.arsene.erp.v1.service.MessageService;
import rw.arsene.erp.v1.service.PayrollService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessageServiceImpl implements MessageService {
    
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final EmployeeService employeeService;
    private final PayrollService payrollService;
    
    @Override
    public MessageDTO createMessage(MessageDTO messageDTO) {
        log.info("Creating new message for employee ID: {}", messageDTO.getEmployeeId());
        
        // Validate employee exists
        Employee employee = employeeService.getEmployeeEntityById(messageDTO.getEmployeeId());
        
        Message message = messageMapper.toEntity(messageDTO);
        Message savedMessage = messageRepository.save(message);
        
        log.info("Message created successfully with ID: {}", savedMessage.getId());
        return messageMapper.toDTO(savedMessage);
    }
    
    @Override
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        log.info("Updating message with ID: {}", id);
        
        Message existingMessage = getMessageEntityById(id);
        
        // Check if message is already sent
        if (existingMessage.getStatus() == MessageStatus.SENT) {
            throw new BusinessException("Cannot update sent message");
        }
        
        // Update fields
        existingMessage.setMessage(messageDTO.getMessage());
        existingMessage.setMonthYear(messageDTO.getMonthYear());
        existingMessage.setStatus(messageDTO.getStatus());
        existingMessage.setErrorMessage(messageDTO.getErrorMessage());
        
        Message updatedMessage = messageRepository.save(existingMessage);
        log.info("Message updated successfully with ID: {}", updatedMessage.getId());
        
        return messageMapper.toDTO(updatedMessage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MessageDTO getMessageById(Long id) {
        Message message = getMessageEntityById(id);
        return messageMapper.toDTO(message);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return messageMapper.toDTOList(messages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getAllMessages(Pageable pageable) {
        Page<Message> messages = messageRepository.findAll(pageable);
        return messages.map(messageMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesByEmployeeId(Long employeeId) {
        List<Message> messages = messageRepository.findByEmployeeId(employeeId);
        return messageMapper.toDTOList(messages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessagesByEmployeeId(Long employeeId, Pageable pageable) {
        Page<Message> messages = messageRepository.findByEmployeeId(employeeId, pageable);
        return messages.map(messageMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesByStatus(MessageStatus status) {
        List<Message> messages = messageRepository.findByStatus(status);
        return messageMapper.toDTOList(messages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessagesByStatus(MessageStatus status, Pageable pageable) {
        Page<Message> messages = messageRepository.findByStatus(status, pageable);
        return messages.map(messageMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesByPeriod(String monthYear) {
        List<Message> messages = messageRepository.findByMonthYear(monthYear);
        return messageMapper.toDTOList(messages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessagesByPeriod(String monthYear, Pageable pageable) {
        Page<Message> messages = messageRepository.findByMonthYear(monthYear, pageable);
        return messages.map(messageMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> searchMessages(String keyword, Pageable pageable) {
        Page<Message> messages = messageRepository.searchMessages(keyword, pageable);
        return messages.map(messageMapper::toDTO);
    }
    
    @Override
    public MessageDTO sendMessage(Long id) {
        log.info("Sending message with ID: {}", id);
        Message message = getMessageEntityById(id);
        
        if (message.getStatus() == MessageStatus.SENT) {
            throw new BusinessException("Message already sent");
        }
        
        try {
            // Simulate sending message (email, SMS, etc.)
            // In real implementation, integrate with email/SMS service
            boolean sent = simulateMessageSending(message);
            
            if (sent) {
                message.setStatus(MessageStatus.SENT);
                message.setSentAt(LocalDateTime.now());
                message.setErrorMessage(null);
            } else {
                message.setStatus(MessageStatus.FAILED);
                message.setErrorMessage("Failed to send message");
            }
            
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
            message.setStatus(MessageStatus.FAILED);
            message.setErrorMessage(e.getMessage());
        }
        
        Message updatedMessage = messageRepository.save(message);
        return messageMapper.toDTO(updatedMessage);
    }
    
    @Override
    public MessageDTO markAsFailed(Long id, String errorMessage) {
        log.info("Marking message as failed with ID: {}", id);
        Message message = getMessageEntityById(id);
        
        message.setStatus(MessageStatus.FAILED);
        message.setErrorMessage(errorMessage);
        
        Message updatedMessage = messageRepository.save(message);
        return messageMapper.toDTO(updatedMessage);
    }
    
    @Override
    public MessageDTO retrySendMessage(Long id) {
        log.info("Retrying to send message with ID: {}", id);
        Message message = getMessageEntityById(id);
        
        if (message.getStatus() != MessageStatus.FAILED) {
            throw new BusinessException("Only failed messages can be retried");
        }
        
        message.setStatus(MessageStatus.PENDING);
        message.setErrorMessage(null);
        Message updatedMessage = messageRepository.save(message);
        
        // Try to send again
        return sendMessage(updatedMessage.getId());
    }
    
    @Override
    public List<MessageDTO> sendPayslipNotifications(Integer month, Integer year) {
        log.info("Sending payslip notifications for period {}/{}", month, year);
        
        List<PayslipDTO> payslips = payrollService.getPayslipsByPeriod(month, year);
        List<MessageDTO> sentMessages = new ArrayList<>();
        
        for (PayslipDTO payslip : payslips) {
            try {
                MessageDTO messageDTO = sendPayslipNotificationToEmployee(payslip.getEmployeeId(), month, year);
                sentMessages.add(messageDTO);
            } catch (Exception e) {
                log.error("Error sending payslip notification to employee {}: {}", 
                        payslip.getEmployeeCode(), e.getMessage());
            }
        }
        
        log.info("Sent {} payslip notifications for period {}/{}", sentMessages.size(), month, year);
        return sentMessages;
    }
    
    @Override
    public MessageDTO sendPayslipNotificationToEmployee(Long employeeId, Integer month, Integer year) {
        log.info("Sending payslip notification to employee ID: {} for period {}/{}", employeeId, month, year);
        
        Employee employee = employeeService.getEmployeeEntityById(employeeId);
        PayslipDTO payslip = payrollService.getPayslipsByEmployeeId(employeeId).stream()
                .filter(p -> p.getMonth().equals(month) && p.getYear().equals(year))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payslip", "employee and period", 
                        employeeId + " for " + month + "/" + year));
        
        String monthYear = String.format("%02d/%d", month, year);
        String messageContent = String.format(
                "Dear %s,\n\nYour payslip for %s is ready.\n\n" +
                "Gross Salary: %s\nNet Salary: %s\n\n" +
                "Please log in to the system to view your complete payslip.\n\n" +
                "Best regards,\nHR Department",
                employee.getFullName(),
                monthYear,
                payslip.getGrossSalary(),
                payslip.getNetSalary()
        );
        
        // Check if message already exists
        messageRepository.findByEmployeeIdAndMonthYear(employeeId, monthYear)
                .ifPresent(messageRepository::delete);
        
        Message message = Message.builder()
                .employee(employee)
                .message(messageContent)
                .monthYear(monthYear)
                .status(MessageStatus.PENDING)
                .build();
        
        Message savedMessage = messageRepository.save(message);
        
        // Try to send the message immediately
        return sendMessage(savedMessage.getId());
    }
    
    @Override
    public void deleteMessage(Long id) {
        log.info("Deleting message with ID: {}", id);
        Message message = getMessageEntityById(id);
        
        messageRepository.delete(message);
        log.info("Message deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(MessageStatus status) {
        return messageRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByPeriod(String monthYear) {
        return messageRepository.countByMonthYear(monthYear);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Message getMessageEntityById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
    }
    
    private boolean simulateMessageSending(Message message) {
        // Simulate message sending logic
        // In real implementation, this would integrate with:
        // - Email service (SendGrid, AWS SES, etc.)
        // - SMS service (Twilio, etc.)
        // - Push notification service
        
        try {
            // Simulate processing time
            Thread.sleep(100);
            
            // Simulate 90% success rate
            return Math.random() > 0.1;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}