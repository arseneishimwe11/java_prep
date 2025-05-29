package rw.arsene.erp.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.dto.PayslipDTO;
import rw.arsene.erp.v1.entity.Payslip;
import rw.arsene.erp.v1.enums.PayslipStatus;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.repository.PayslipRepository;
import rw.arsene.erp.v1.service.PayslipService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PayslipServiceImpl implements PayslipService {

    @Autowired
    private PayslipRepository payslipRepository;

    // Payslip creation is handled by PayrollService

    @Override
    @Transactional(readOnly = true)
    public Optional<PayslipDTO> getPayslipById(String id) {
        return payslipRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PayslipDTO> getPayslipByEmployeeMonthYear(String employeeCode, int month, int year) {
        return payslipRepository.findByEmployee_CodeAndMonthAndYear(employeeCode, month, year).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getPayslipsByEmployee(String employeeCode) {
        return payslipRepository.findByEmployee_Code(employeeCode).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getPayslipsByMonthYear(int month, int year) {
        return payslipRepository.findByMonthAndYear(month, year).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayslipDTO> getAllPayslips() {
        return payslipRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PayslipDTO updatePayslipStatus(String id, String statusStr) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip", "id", id));

        try {
            PayslipStatus newStatus = PayslipStatus.valueOf(statusStr.toUpperCase());
            payslip.setStatus(newStatus);
            Payslip updatedPayslip = payslipRepository.save(payslip);
            // Trigger messaging service if status is PAID
            // This logic should ideally be in a dedicated PayrollService or event listener
            // if (newStatus == PayslipStatus.PAID) {
            //     // Trigger message generation/sending
            // }
            return mapToDTO(updatedPayslip);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + statusStr);
        }
    }

    // --- Mappers --- //

    @Override
    public PayslipDTO mapToDTO(Payslip payslip) {
        PayslipDTO dto = new PayslipDTO();
        dto.setId(payslip.getId());
        if (payslip.getEmployee() != null) {
            dto.setEmployeeCode(payslip.getEmployee().getCode());
            dto.setEmployeeName(payslip.getEmployee().getFirstName() + " " + payslip.getEmployee().getLastName());
        }
        dto.setHouseAmount(payslip.getHouseAmount());
        dto.setTransportAmount(payslip.getTransportAmount());
        dto.setEmployeeTaxAmount(payslip.getEmployeeTaxAmount());
        dto.setPensionAmount(payslip.getPensionAmount());
        dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        dto.setOtherTaxAmount(payslip.getOtherTaxAmount());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setStatus(payslip.getStatus());
        return dto;
    }

    @Override
    public Payslip mapToEntity(PayslipDTO payslipDTO) {
        // This mapping is less common as payslips are usually generated, not created from DTOs
        Payslip payslip = new Payslip();
        payslip.setId(payslipDTO.getId()); // Handle ID carefully
        // Need to fetch Employee entity based on employeeCode
        // Employee employee = employeeRepository.findById(payslipDTO.getEmployeeCode()).orElse(null);
        // payslip.setEmployee(employee);
        payslip.setHouseAmount(payslipDTO.getHouseAmount());
        payslip.setTransportAmount(payslipDTO.getTransportAmount());
        payslip.setEmployeeTaxAmount(payslipDTO.getEmployeeTaxAmount());
        payslip.setPensionAmount(payslipDTO.getPensionAmount());
        payslip.setMedicalInsuranceAmount(payslipDTO.getMedicalInsuranceAmount());
        payslip.setOtherTaxAmount(payslipDTO.getOtherTaxAmount());
        payslip.setGrossSalary(payslipDTO.getGrossSalary());
        payslip.setNetSalary(payslipDTO.getNetSalary());
        payslip.setMonth(payslipDTO.getMonth());
        payslip.setYear(payslipDTO.getYear());
        payslip.setStatus(payslipDTO.getStatus());
        return payslip;
    }
}
