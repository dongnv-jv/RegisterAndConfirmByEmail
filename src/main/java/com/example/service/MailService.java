package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String LINK = "link";
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public String processContext(String link) {
        Locale locale = Locale.forLanguageTag("vi");
        Context context = new Context(locale);
        context.setVariable(LINK, link);
        return springTemplateEngine.process("sendMail", context);
    }

    @Async("asyncExecutor")
    public void sendMail(String mail, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String link = "http://localhost:8080/api/v1/auth/confirm/" + token;

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            message.setTo(mail);
            message.setSubject("XÁC NHẬN ĐĂNG KÝ TÀI KHOẢN ");
            message.setText(processContext(link), true);
            javaMailSender.send(mimeMessage);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

}
