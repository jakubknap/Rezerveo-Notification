package pl.rezerveo.notification.mail.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.rezerveo.notification.mail.MailMessage;
import pl.rezerveo.notification.properties.MailProperties;
import pl.rezerveo.notification.util.MaskingUtil;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private static final String MAIL_SUCCESSFULLY_SENT = "Mail successfully sent";
    private static final String ERROR_WHILE_SENDING_MAIL = "Error while sending mail: {}";

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final TemplateEngine templateEngine;

    public void sendMailAsync(MailMessage message) {
        CompletableFuture.runAsync(() -> sendHtml(message));
    }

    public void sendMail(MailMessage message) {
        sendHtml(message);
    }

    public void sendPlainMailAsync(String receiverEmail, String subject, String body) {
        CompletableFuture.runAsync(() -> sendPlain(new String[]{receiverEmail}, subject, body));
    }

    public void sendPlainMail(String receiverEmail, String subject, String body) {
        sendPlain(new String[]{receiverEmail}, subject, body);
    }

    private void sendHtml(MailMessage message) {
        String[] to = message.getReceivers();
        String subject = message.getSubject();

        if (!isReadyToSend(to, subject)) {
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(prepareSender());
            helper.setTo(to);
            mimeMessage.setSubject(subject);

            Context context = new Context();
            context.setVariables(message.getVariables());

            String htmlContent = templateEngine.process("mail/" + message.getTemplateName(), context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info(MAIL_SUCCESSFULLY_SENT);
        } catch (Exception ex) {
            log.error(ERROR_WHILE_SENDING_MAIL, ex.getMessage(), ex);
        }
    }

    private void sendPlain(String[] to, String subject, String mailBody) {
        if (!isReadyToSend(to, subject)) {
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom(prepareSender());
            helper.setTo(to);
            message.setSubject(subject);
            helper.setText(mailBody, false);

            mailSender.send(message);

            log.info(MAIL_SUCCESSFULLY_SENT);
        } catch (Exception ex) {
            log.error(ERROR_WHILE_SENDING_MAIL, ex.getMessage(), ex);
        }
    }

    private boolean isReadyToSend(String[] to, String subject) {
        if (isEmpty(to) || Arrays.stream(to).anyMatch(Objects::isNull)) {
            log.error("The receiver of the e-mail was not detected. Mail will not be sent.");
            return false;
        }

        final String maskedEmails = String.join(", ", maskEmails(to));

        if (mailProperties.isMailingDisabled()) {
            log.error("Mail has NOT been sent - Mail sending is globally disabled. Mail to: [{}] | Subject: [{}]", maskedEmails, subject);
            return false;
        }

        log.info("Preparing to send mail to: [{}] | Subject: [{}]", maskedEmails, subject);
        return true;
    }

    private String[] maskEmails(String[] emails) {
        return Arrays.stream(emails)
                     .map(MaskingUtil::maskEmail)
                     .toArray(String[]::new);
    }

    private InternetAddress prepareSender() throws UnsupportedEncodingException {
        return new InternetAddress(mailProperties.getSenderMail(), mailProperties.getSenderName());
    }
}