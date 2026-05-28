/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author aimeb
 */
public class EmailUtil {
    
    
    private static final String SENDER_EMAIL = "aimeb909@gmail.com";
    private static final String SENDER_PASSWORD = "risxfgtwzrqeduge";

    public static boolean sendOTPEmail(String recipientEmail, String otp) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail));
            message.setSubject("Urban Bus Fleet - OTP Verification");
            message.setText(
                "Dear User,\n\n"
                + "Your OTP verification code is:\n\n"
                + "        " + otp + "\n\n"
                + "This code expires in 5 minutes.\n"
                + "Do not share this code with anyone.\n\n"
                + "Urban Bus Fleet Management System"
            );

            Transport.send(message);
            System.out.println("OTP email sent to: " + recipientEmail);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean sendNotificationEmail(
        String recipientEmail,
        String subject,
        String body) {
    try {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication
                        getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
        return true;

    } catch (Exception e) {
        System.err.println("Notification email failed: "
            + e.getMessage());
        return false;
    }
}
    
}
