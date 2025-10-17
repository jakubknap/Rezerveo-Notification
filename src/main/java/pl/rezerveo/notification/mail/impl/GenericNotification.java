package pl.rezerveo.notification.mail.impl;

import pl.rezerveo.notification.mail.MailMessage;

import java.util.Map;

public record GenericNotification(String email, String subject, String message) implements MailMessage {

    @Override
    public String[] getReceivers() {
        return new String[]{email};
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getTemplateName() {
        return "generic-notification";
    }

    @Override
    public Map<String, Object> getVariables() {
        return Map.of("subject", subject,
                      "message", message);
    }
}