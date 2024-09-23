package com.esoft.citytaxi.services;


import com.esoft.citytaxi.dto.EmailMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfig;


    public MailService(JavaMailSender javaMailSender,
                       Configuration freemarkerConfig) {
        this.javaMailSender = javaMailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    @Async
    public CompletableFuture<Void> sendEmail(EmailMapper emailMapper) throws MessagingException, IOException, TemplateException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        msg.setFrom(sender);
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(emailMapper.getTo());
        helper.setSubject(emailMapper.getSubject());
        String basePackagePath = "/templates/email/";
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), basePackagePath);
        Template t = freemarkerConfig.getTemplate(emailMapper.getTemplateName());
        String emailTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(t, emailMapper.getModel());
        helper.setText(emailTemplate, true);
        javaMailSender.send(msg);
        log.info("Email Sent Successfully.");
        return CompletableFuture.completedFuture(null);
    }
}
