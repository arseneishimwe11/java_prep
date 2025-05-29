package rw.arsene.erp.v1.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rw.arsene.erp.v1.dto.MessageDTO;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.Message;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    
    private final EmployeeRepository employeeRepository;
    
    public MessageDTO toDTO(Message message) {
        if (message == null) {
            return null;
        }
        
        Employee employee = message.getEmployee();
        
        return MessageDTO.builder()
                .id(message.getId())
                .employeeId(employee.getId())
                .employeeCode(employee.getCode())
                .employeeName(employee.getFullName())
                .employeeEmail(employee.getEmail())
                .message(message.getMessage())
                .monthYear(message.getMonthYear())
                .status(message.getStatus())
                .sentAt(message.getSentAt())
                .errorMessage(message.getErrorMessage())
                .sent(message.isSent())
                .failed(message.isFailed())
                .build();
    }
    
    public Message toEntity(MessageDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getEmployeeId()));
        
        return Message.builder()
                .id(dto.getId())
                .employee(employee)
                .message(dto.getMessage())
                .monthYear(dto.getMonthYear())
                .status(dto.getStatus())
                .errorMessage(dto.getErrorMessage())
                .build();
    }
    
    public List<MessageDTO> toDTOList(List<Message> messages) {
        return messages.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Message> toEntityList(List<MessageDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}