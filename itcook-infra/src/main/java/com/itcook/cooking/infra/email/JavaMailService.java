package com.itcook.cooking.infra.email;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class JavaMailService implements AuthCodeService{

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    @TransactionalEventListener
    public void sentAuthCode(EmailSendEvent emailSendEvent) {
        log.info("mail 시도 ");
        try {
            String from = "hangs0908@gmail.com";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(emailSendEvent.getTo());
            mimeMessageHelper.setSubject(emailSendEvent.getSubject());
            mimeMessageHelper.setText(emailSendEvent.getBody(), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.info("메일 전송 실패 ", e);
        }

    }
}
