package com.itcook.cooking.infra.email.javamail;

import com.itcook.cooking.infra.email.AuthCodeService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class JavaMailService implements AuthCodeService {

    private final JavaMailSender javaMailSender;
    private static final String SENDER_EMAIL = "noreplycook@gmail.com";

    @Override
    @Async
    @TransactionalEventListener
    public void sentAuthCode(EmailSendEvent emailSendEvent) {
        log.info("메일 전송 시도 ");
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(SENDER_EMAIL);
            mimeMessageHelper.setTo(emailSendEvent.getTo());
            mimeMessageHelper.setSubject(emailSendEvent.getSubject());
            mimeMessageHelper.setText(emailSendEvent.getBody(), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("메일 전송 실패 ", e);
        }

    }
}
