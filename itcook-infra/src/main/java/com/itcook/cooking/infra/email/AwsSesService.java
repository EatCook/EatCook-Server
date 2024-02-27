package com.itcook.cooking.infra.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsSesService implements AuthCodeService{

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final String from = "eatcook.noreply@gmail.com";

    @Override
    @Async
    @TransactionalEventListener
    public void sentAuthCode(EmailSendEvent emailSendEvent) {
        log.info("{}", TransactionSynchronizationManager.getCurrentTransactionName());
        log.info("{}", TransactionSynchronizationManager.isSynchronizationActive());
        try {
        SendEmailRequest sendEmailRequest = createSendEmailRequest(emailSendEvent);
        amazonSimpleEmailService.sendEmail(sendEmailRequest);
        } catch (MailException e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    private SendEmailRequest createSendEmailRequest(EmailSendEvent emailSendEvent) {
        return new SendEmailRequest()
            .withDestination(new Destination().withToAddresses(emailSendEvent.getTo()))
            .withSource(from)
            .withMessage(new Message()
                .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(
                    emailSendEvent.getSubject()))
                .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(
                    emailSendEvent.getBody())))
            );
    }
}
