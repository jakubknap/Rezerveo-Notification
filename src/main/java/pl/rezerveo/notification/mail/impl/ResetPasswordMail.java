package pl.rezerveo.notification.mail.impl;

import pl.rezerveo.notification.mail.MailMessage;

import java.util.Map;

public record ResetPasswordMail(String email, String userFullName, String activationLink) implements MailMessage {

    @Override
    public String[] getReceivers() {
        return new String[]{email};
    }

    @Override
    public String getSubject() {
        return "Rezerveo - Resetowanie hasła";
    }

    @Override
    public String getTemplateName() {
        return "password-reset";
    }

    @Override
    public Map<String, Object> getVariables() {
        return Map.of("userFullName", userFullName,
                      "resetPasswordLink", activationLink);
    }
}