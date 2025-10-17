package pl.rezerveo.notification.webSocket;

import java.time.LocalDateTime;

public record NotificationMessage(
        String title,
        String content,
        String userEmail,
        String timestamp
) {
    public NotificationMessage(String title, String content, String userEmail) {
        this(title,
             content,
             userEmail,
             LocalDateTime.now().toString());
    }
}