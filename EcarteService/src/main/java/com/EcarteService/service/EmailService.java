package com.EcarteService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Sends a general email with the given subject and message to the specified recipient.
     */
    public void sendEmail(String to, String subject, String message) {
        try {
            // Construct the URL with query parameters
            String emailUrl = "http://localhost:1013/email/send?to=" + to + "&subject=" + subject + "&message=" + message;

            // Send POST request with no body (query params only)
            restTemplate.postForObject(emailUrl, null, String.class);

            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    /**
     * Sends a notification email for an upcoming recurring payment.
     */
    /*public void sendRecurringPaymentNotification(String to, double amount, String description, String nextExecutionDate) {
        String subject = "Reminder: Upcoming Recurring Payment";
        String message = String.format(
                "Dear User,\n\nThis is a reminder for your upcoming payment of $%.2f for '%s' scheduled on %s.\n\nThank you.",
                amount, description, nextExecutionDate
        );

        sendEmail(to, subject, message);
    }*/

    /**
     * Sends a confirmation email for a successfully processed recurring payment.
     */
    public void sendPaymentConfirmation(String to, double amount, String description) {
        String subject = "Payment Successful";
        String message = String.format(
                "Dear User,\n\nYour payment of $%.2f for '%s' has been successfully processed.\n\nThank you.",
                amount, description
        );

        sendEmail(to, subject, message);
    }

    /**
     * Sends a notification email after any successful transaction is completed.
     */
    public void sendTransactionCompletionEmail(String to, double amount, String senderNumeroCarte, String receiverNumeroCarte, String description) {
        String subject = "Transaction Successful";
        String message = String.format(
                "Dear User,\n\nYour transaction has been successfully completed.\n\n" +
                        "Details:\n" +
                        "- Amount: $%.2f\n" +
                        "- Sender Card: %s\n" +
                        "- Receiver Card: %s\n" +
                        "- Description: %s\n\n" +
                        "Thank you for using our service.",
                amount, senderNumeroCarte, receiverNumeroCarte, description
        );

        sendEmail(to, subject, message);
    }
}
