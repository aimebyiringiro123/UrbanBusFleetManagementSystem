/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.notification;

import java.io.Serializable;

/**
 *
 * @author aimeb
 */
public class NotificationMessage implements Serializable {

    private String type;
    private String recipient;
    private String subject;
    private String body;

    public NotificationMessage() {}

    public NotificationMessage(String type, String recipient,
            String subject, String body) {
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    @Override
    public String toString() {
        return "NotificationMessage{type=" + type
            + ", recipient=" + recipient + "}";
    }
}