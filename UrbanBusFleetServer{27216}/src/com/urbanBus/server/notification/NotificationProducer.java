/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.notification;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author aimeb
 */
public class NotificationProducer {

    private static final String BROKER_URL =
        "tcp://localhost:61616";
    private static final String QUEUE_NAME =
        "urban.bus.notifications";

    public static void sendNotification(
            NotificationMessage message) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(
                false, Session.AUTO_ACKNOWLEDGE);
            Destination destination =
                session.createQueue(QUEUE_NAME);
            MessageProducer producer =
                session.createProducer(destination);
            producer.setDeliveryMode(
                DeliveryMode.NON_PERSISTENT);

            String messageText =
                message.getType() + "|"
                + message.getRecipient() + "|"
                + message.getSubject() + "|"
                + message.getBody();

            TextMessage textMessage =
                session.createTextMessage(messageText);
            producer.send(textMessage);

            System.out.println("✅ Notification queued: "
                + message.getType()
                + " → " + message.getRecipient());
            session.close();

        } catch (Exception e) {
            System.err.println(
                "❌ Failed to queue notification: "
                + e.getMessage());
        } finally {
            if (connection != null) {
                try { connection.close(); }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    // ── Ready-made notification methods ──

    public static void sendMaintenanceAlert(
            String adminEmail, String busPlate) {
        NotificationMessage msg = new NotificationMessage(
            "MAINTENANCE_ALERT",
            adminEmail,
            "⚠️ Bus Maintenance Overdue - " + busPlate,
            "Dear Maintenance Officer,\n\n"
            + "Bus with plate number " + busPlate
            + " has been flagged as overdue for maintenance.\n"
            + "Please schedule a service immediately.\n\n"
            + "Urban Bus Fleet Management System"
        );
        sendNotification(msg);
    }

    public static void sendLicenseExpiryAlert(
            String adminEmail,
            String driverName,
            String expiryDate) {
        NotificationMessage msg = new NotificationMessage(
            "LICENSE_EXPIRY_ALERT",
            adminEmail,
            "⚠️ Driver License Expiring - " + driverName,
            "Dear Administrator,\n\n"
            + "Driver " + driverName
            + "'s license is expiring on "
            + expiryDate + ".\n"
            + "Please remind the driver to renew "
            + "their license before it expires.\n\n"
            + "Urban Bus Fleet Management System"
        );
        sendNotification(msg);
    }

    public static void sendTripCompletedAlert(
            String adminEmail,
            String routeName,
            String driverName,
            String busPlate) {
        NotificationMessage msg = new NotificationMessage(
            "TRIP_COMPLETED",
            adminEmail,
            "✅ Trip Completed - " + routeName,
            "Dear Administrator,\n\n"
            + "A trip has been completed successfully.\n\n"
            + "Route:  " + routeName + "\n"
            + "Driver: " + driverName + "\n"
            + "Bus:    " + busPlate + "\n\n"
            + "Urban Bus Fleet Management System"
        );
        sendNotification(msg);
    }

    public static void sendNewUserAlert(
            String adminEmail,
            String newUserName,
            String role) {
        NotificationMessage msg = new NotificationMessage(
            "NEW_USER_CREATED",
            adminEmail,
            "👤 New Staff Account Created - " + newUserName,
            "Dear Administrator,\n\n"
            + "A new staff account has been created:\n\n"
            + "Name: " + newUserName + "\n"
            + "Role: " + role + "\n\n"
            + "Urban Bus Fleet Management System"
        );
        sendNotification(msg);
    }
}