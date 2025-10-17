package pl.rezerveo.notification.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.rezerveo.notification.encryption.EncryptionService;
import pl.rezerveo.notification.event.MailEvent;
import pl.rezerveo.notification.mail.impl.AccountActivationMail;
import pl.rezerveo.notification.mail.impl.ResetPasswordMail;
import pl.rezerveo.notification.mail.service.MailService;
import pl.rezerveo.notification.properties.FrontendProperties;

import static pl.rezerveo.notification.config.RabbitConfig.MAIL_EVENT_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailEventListener {

    private final EncryptionService encryptionService;
    private final MailService mailService;
    private final FrontendProperties frontendProperties;

    @RabbitListener(queues = MAIL_EVENT_QUEUE)
    public void handleMailEvent(MailEvent event) {
        log.info("Received MailEvent from RabbitMQ: {}", event);

        String userEmail = encryptionService.decrypt(event.userEmail());
        String userFirstName = encryptionService.decrypt(event.userFirstName());
        String userLastName = encryptionService.decrypt(event.userLastName());
        String userFullName = userFirstName + " " + userLastName;
        String token = event.token();

        switch (event.mailType()) {
        case ACCOUNT_ACTIVATION: {
            mailService.sendMail(new AccountActivationMail(userEmail, userFullName, frontendProperties.prepareAccountActivationLink(token)));
            break;
        }
        case PASSWORD_RESET: {
            mailService.sendMail(new ResetPasswordMail(userEmail, userFullName, frontendProperties.prepareResetPasswordLink(token)));
            break;
        }
        }
    }
}