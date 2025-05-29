package rw.arsene.erp.v1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rw.arsene.erp.v1.entity.Payslip;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.institution:Government of Rwanda}")
    private String institutionName;

    @Async
    public void sendPaymentNotification(Payslip payslip) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(payslip.getEmployee().getEmail());
            message.setSubject("Salary Payment Notification - " + payslip.getMonth() + "/" + payslip.getYear());

            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Your salary for %d/%d from %s has been processed successfully.\n\n" +
                "Payment Details:\n" +
                "Employee ID: %s\n" +
                "Gross Salary: %s RWF\n" +
                "Net Salary: %s RWF\n" +
                "Month/Year: %d/%d\n\n" +
                "The amount of %s RWF has been credited to your account.\n\n" +
                "Best regards,\n" +
                "HR Department\n" +
                "%s",
                payslip.getEmployee().getFirstName(),
                payslip.getMonth(),
                payslip.getYear(),
                institutionName,
                payslip.getEmployee().getCode(),
                payslip.getGrossSalary(),
                payslip.getNetSalary(),
                payslip.getMonth(),
                payslip.getYear(),
                payslip.getNetSalary(),
                institutionName
            );

            message.setText(emailBody);

            mailSender.send(message);
            log.info("Payment notification email sent to: {}", payslip.getEmployee().getEmail());

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", payslip.getEmployee().getEmail(), e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String email, String firstName, String temporaryPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to " + institutionName + " ERP System");

            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Welcome to %s Employee Management System!\n\n" +
                "Your account has been created successfully.\n" +
                "Login Credentials:\n" +
                "Email: %s\n" +
                "Temporary Password: %s\n\n" +
                "Please login and change your password immediately.\n\n" +
                "Best regards,\n" +
                "HR Department\n" +
                "%s",
                firstName,
                institutionName,
                email,
                temporaryPassword,
                institutionName
            );

            message.setText(emailBody);

            mailSender.send(message);
            log.info("Welcome email sent to: {}", email);

        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", email, e.getMessage());
        }
    }
}
