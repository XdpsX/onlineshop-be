package com.xdpsx.onlineshop.utils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.xdpsx.onlineshop.entities.enums.EmailTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    // TODO remove async later
    @Async
    public void sendEmail(String to, EmailTemplate emailTemplate, Map<String, Object> properties)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(properties);

        String template = templateEngine.process(emailTemplate.getTemplateName(), context);

        helper.setFrom("no-reply@xdpsx.com");
        helper.setTo(to);
        helper.setSubject(emailTemplate.getSubject());
        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }
}
