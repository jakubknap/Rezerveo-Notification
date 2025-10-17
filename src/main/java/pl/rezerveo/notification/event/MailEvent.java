package pl.rezerveo.notification.event;

public record MailEvent(String userEmail, String userFirstName, String userLastName, String token, MailType mailType) {}