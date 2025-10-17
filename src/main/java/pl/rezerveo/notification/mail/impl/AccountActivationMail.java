package pl.rezerveo.notification.mail.impl;

import pl.rezerveo.notification.mail.MailMessage;

import java.util.Map;

public record AccountActivationMail(String email, String userFullName, String activationLink) implements MailMessage {

    @Override
    public String[] getReceivers() {
        return new String[]{email};
    }

    @Override
    public String getSubject() {
        return "Rezerveo - Aktywacja konta";
    }

    @Override
    public String getTemplateName() {
        return "account-activation";
    }

    @Override
    public Map<String, Object> getVariables() {
        return Map.of("userFullName", userFullName,
                      "activationLink", activationLink);
    }
}