package rw.arsene.erp.v1.mapper;

import org.springframework.stereotype.Component;
import rw.arsene.erp.v1.dto.DeductionDTO;
import rw.arsene.erp.v1.entity.Deduction;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeductionMapper {
    
    public DeductionDTO toDTO(Deduction deduction) {
        if (deduction == null) {
            return null;
        }
        
        return DeductionDTO.builder()
                .id(deduction.getId())
                .code(deduction.getCode())
                .deductionName(deduction.getDeductionName())
                .percentage(deduction.getPercentage())
                .description(deduction.getDescription())
                .isActive(deduction.getIsActive())
                .createdAt(deduction.getCreatedAt())
                .updatedAt(deduction.getUpdatedAt())
                .build();
    }
    
    public Deduction toEntity(DeductionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Deduction.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .deductionName(dto.getDeductionName())
                .percentage(dto.getPercentage())
                .description(dto.getDescription())
                .isActive(dto.getIsActive())
                .build();
    }
    
    public List<DeductionDTO> toDTOList(List<Deduction> deductions) {
        return deductions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Deduction> toEntityList(List<DeductionDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}