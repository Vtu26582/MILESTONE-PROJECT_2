package com.jobportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * MOCK EMAIL SERVICE: Simulates sending an email by logging it securely to the console.
     * This avoids needing real SMTP credentials while demonstrating the architectural flow for review.
     * @param to recipient email
     * @param subject email subject
     * @param text email body
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        logger.info("\n=======================================================");
        logger.info("📧 MOCK EMAIL NOTIFICATION DISPATCHED (Via AGI Engine) 📧");
        logger.info("=======================================================");
        logger.info("TO: {}", to);
        logger.info("SUBJECT: {}", subject);
        logger.info("MESSAGE:");
        logger.info(text);
        logger.info("=======================================================\n");
    }
}
