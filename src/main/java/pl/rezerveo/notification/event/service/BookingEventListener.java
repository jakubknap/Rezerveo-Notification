package pl.rezerveo.notification.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.rezerveo.notification.encryption.EncryptionService;
import pl.rezerveo.notification.event.BookingEvent;
import pl.rezerveo.notification.mail.impl.GenericNotification;
import pl.rezerveo.notification.mail.service.MailService;

import static pl.rezerveo.notification.config.RabbitConfig.BOOKING_EVENT_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventListener {

    private final EncryptionService encryptionService;
    private final MailService mailService;

    @RabbitListener(queues = BOOKING_EVENT_QUEUE)
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received BookingEvent from RabbitMQ: {}", event);

        String targetEmail = encryptionService.decrypt(event.targetEmail());
        String message = encryptionService.decrypt(event.message());

        mailService.sendMail(new GenericNotification(targetEmail, event.title(), message));
    }
}