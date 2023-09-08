package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.dto.MailRequest;
import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.model.NotificationEmail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private final Configuration configuration;

    @Async
    void setMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("lamchanhiephceet@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(
                    notificationEmail.getBody()
            ));
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Đã gửi email kích hoạt");
        } catch (MailException ex) {
            throw new SpringTrelloException("Đã xảy ra lỗi trong quá trình gửi thư "
                    +  notificationEmail.getRecipient());
        }
    }

    public void sendMail(MailRequest request, Map<String, Object> model, String mailTemplate){
        System.out.println("Sending email....");
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = configuration.getTemplate(mailTemplate);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);

            helper.setTo(request.getTo());
            helper.setText(html,true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mailSender.send(message);
            System.out.println("Sent");
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

}