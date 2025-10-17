package pl.rezerveo.notification.webSocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToAll(NotificationMessage message) {
        log.info("Sending WebSocket notification");
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}