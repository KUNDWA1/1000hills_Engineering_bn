package com.thousandhills.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.thousandhills.backend.dto.EmailNotificationPayload;
import com.thousandhills.backend.model.Order;
import com.thousandhills.backend.model.QuotationRequest;
import com.thousandhills.backend.model.VendorProfile;

@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailNotificationService(JavaMailSender mailSender,
                                    @Value("${spring.mail.from:${spring.mail.username:noreply@1000hills.com}}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendEmail(EmailNotificationPayload payload) {
        if (payload.getTo() == null || payload.getTo().isBlank()) {
            log.warn("Skipping email send because recipient is missing: {}", payload);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(payload.getTo());
            helper.setSubject(payload.getSubject());
            helper.setText(payload.getBody(), true);
            mailSender.send(message);
            log.info("Email sent to {}: {}", payload.getTo(), payload.getSubject());
        } catch (MessagingException exception) {
            log.error("Failed to send email to {}", payload.getTo(), exception);
            log.info("Fallback email payload: {}", payload);
        }
    }

    public void notifyVendorApproved(VendorProfile vendor) {
        EmailNotificationPayload payload = new EmailNotificationPayload(
                vendor.getUser() != null ? vendor.getUser().getEmail() : "",
                "1000Hills: Vendor Approved",
                String.format("Hello %s,<br/><br/>Your vendor account has been approved. You can now add products and receive assignments.<br/><br/>Best regards,<br/>1000Hills Engineering", vendor.getCompanyName())
        );
        sendEmail(payload);
    }

    public void notifyVendorRejected(VendorProfile vendor) {
        EmailNotificationPayload payload = new EmailNotificationPayload(
                vendor.getUser() != null ? vendor.getUser().getEmail() : "",
                "1000Hills: Vendor Registration Rejected",
                String.format("Hello %s,<br/><br/>Your vendor registration has been rejected. Please review your documents and resubmit your application.<br/><br/>Best regards,<br/>1000Hills Engineering", vendor.getCompanyName())
        );
        sendEmail(payload);
    }

    public void notifyQuotationAssigned(QuotationRequest quotation) {
        EmailNotificationPayload payload = new EmailNotificationPayload(
                quotation.getVendor() != null && quotation.getVendor().getUser() != null ? quotation.getVendor().getUser().getEmail() : "",
                "1000Hills: Quotation Assigned",
                String.format("Hello %s,<br/><br/>A quotation request %s has been assigned to your company. Please review the request and respond with your pricing.<br/><br/>Best regards,<br/>1000Hills Engineering",
                        quotation.getVendor() != null ? quotation.getVendor().getCompanyName() : "Vendor",
                        quotation.getRequestReference())
        );
        sendEmail(payload);
    }

    public void notifyOrderAssigned(Order order) {
        EmailNotificationPayload payload = new EmailNotificationPayload(
                order.getVendor() != null && order.getVendor().getUser() != null ? order.getVendor().getUser().getEmail() : "",
                "1000Hills: Order Assigned",
                String.format("Hello %s,<br/><br/>Order %s has been assigned to your company. Please begin order fulfillment and confirm availability.<br/><br/>Best regards,<br/>1000Hills Engineering",
                        order.getVendor() != null ? order.getVendor().getCompanyName() : "Vendor",
                        order.getOrderReference())
        );
        sendEmail(payload);
    }
}
