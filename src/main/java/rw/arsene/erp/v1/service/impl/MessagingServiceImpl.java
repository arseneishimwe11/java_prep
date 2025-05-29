package rw.arsene.erp.v1.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.javamail.JavaMailSender; // Add if email sending is implemented
// import org.springframework.mail.SimpleMailMessage; // Add if email sending is implemented
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.arsene.erp.v1.entity.Employee;
import rw.arsene.erp.v1.entity.Message;
import rw.arsene.erp.v1.entity.Payslip;
import rw.arsene.erp.v1.enums.MessageSentStatus;
import rw.arsene.erp.v1.exception.ResourceNotFoundException;
import rw.arsene.erp.v1.repository.MessageRepository;
import rw.arsene.erp.v1.service.MessagingService;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class MessagingServiceImpl implements MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingServiceImpl.class);

    @Autowired
    private MessageRepository messageRepository;

    // @Autowired
    // private JavaMailSender emailSender; // Inject if implementing email sending

    // This method should be called when a payslip status is updated to PAID
    // Ideally triggered by an event listener or within the payslip status update logic
    @Override
    @Transactional
    public void generateAndStoreSalaryNotification(Payslip payslip) {
        if (payslip == null || payslip.getEmployee() == null) {
            logger.error("Cannot generate notification for null payslip or employee.");
            return;
        }

        Employee employee = payslip.getEmployee();
        String monthName = Month.of(payslip.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String institutionName = "Government of Rwanda"; // Or fetch dynamically if needed

        // Format: Dear <FIRSTNAME>, your salary for <MONTH/YEAR> from <INSTITUTION> amount <AMOUNT> has been credited to your <EMPLOYEE_ID> account successfully.
        String messageText = String.format(
                "Dear %s, your salary for %s/%d from %s amount %.2f FRW has been credited to your %s account successfully.",
                employee.getFirstName(),
                monthName,
                payslip.getYear(),
                institutionName,
                payslip.getNetSalary(),
                employee.getCode() // Using employee code as account identifier as per requirement
        );

        Message notification = new Message();
        notification.setEmployee(employee);
        notification.setMessage(messageText);
        notification.setMonth(payslip.getMonth());
        notification.setYear(payslip.getYear());
        notification.setSentStatus(MessageSentStatus.PENDING); // Initially pending

        try {
            Message savedMessage = messageRepository.save(notification);
            logger.info("Salary notification stored for employee {} for {}/{}", employee.getCode(), payslip.getMonth(), payslip.getYear());
            // Optionally trigger email sending immediately or via a scheduled task
            // sendSalaryNotificationEmail(savedMessage.getId());
        } catch (Exception e) {
            logger.error("Failed to store salary notification for employee {}: {}", employee.getCode(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void sendSalaryNotificationEmail(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        if (message.getSentStatus() == MessageSentStatus.SENT) {
            logger.warn("Email for message ID {} already sent.", messageId);
            return;
        }

        Employee employee = message.getEmployee();
        if (employee == null || employee.getEmail() == null) {
            logger.error("Cannot send email for message ID {}: Employee or email is null.", messageId);
            message.setSentStatus(MessageSentStatus.FAILED);
            messageRepository.save(message);
            return;
        }

        try {
            // --- Email Sending Logic (Requires spring-boot-starter-mail dependency) ---
            /*
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(employee.getEmail());
            mailMessage.setSubject("Salary Notification - " + Month.of(message.getMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + message.getYear());
            mailMessage.setText(message.getMessage());
            // mailMessage.setFrom("noreply@gov.rw"); // Configure sender email
            emailSender.send(mailMessage);
            */
            // --- End Email Sending Logic ---

            // Simulate sending for now
            logger.info("Simulating email sent to {} for message ID {}", employee.getEmail(), messageId);
            message.setSentStatus(MessageSentStatus.SENT);
            messageRepository.save(message);
            logger.info("Email notification status updated to SENT for message ID {}", messageId);

        } catch (Exception e) {
            logger.error("Failed to send salary notification email for message ID {}: {}", messageId, e.getMessage());
            message.setSentStatus(MessageSentStatus.FAILED);
            messageRepository.save(message);
        }
    }
}
