/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.notification;

import com.urbanBus.server.util.EmailUtil;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author aimeb
 */
public class NotificationConsumer implements Runnable {

    private static final String BROKER_URL =
        "tcp://localhost:61616";
    private static final String QUEUE_NAME =
        "urban.bus.notifications";

    private boolean running = true;

    @Override
    public void run() {
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
            MessageConsumer consumer =
                session.createConsumer(destination);

            System.out.println(
                "Notification consumer started...");
            System.out.println(
                "Listening on queue: " + QUEUE_NAME);

            while (running) {
                Message message = consumer.receive(1000);
                if (message instanceof TextMessage) {
                    TextMessage textMessage =
                        (TextMessage) message;
                    processMessage(textMessage.getText());
                }
            }
            session.close();

        } catch (Exception e) {
            System.err.println(
                "Consumer error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try { connection.close(); }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private void processMessage(String messageText) {
        try {
            String[] parts = messageText.split("\\|", 4);
            if (parts.length < 4) return;

            String type      = parts[0];
            String recipient = parts[1];
            String subject   = parts[2];
            String body      = parts[3];

            System.out.println(
                "Processing: " + type
                + " → " + recipient);

            boolean sent = EmailUtil
                .sendNotificationEmail(
                    recipient, subject, body);

            if (sent) {
                System.out.println(
                    "Email sent to: " + recipient);
            } else {
                System.err.println(
                    "Email failed to: " + recipient);
            }

        } catch (Exception e) {
            System.err.println(
                "Error processing message: "
                + e.getMessage());
        }
    }

    public void stop() {
        this.running = false;
    }
}