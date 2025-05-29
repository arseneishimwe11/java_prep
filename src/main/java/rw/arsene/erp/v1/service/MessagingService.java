package rw.arsene.erp.v1.service;

import rw.arsene.erp.v1.entity.Payslip;

public interface MessagingService {
    void generateAndStoreSalaryNotification(Payslip payslip);
    void sendSalaryNotificationEmail(String messageId); // Or pass the Message entity
}
