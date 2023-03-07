package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async("asyncExecutor")
    public void sendMail(String mail, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String link = "http://localhost:8080/api/v1/auth/confirm/" + token;
        String content = "<h1>Xác nhận email</h1>\n" +
                "<p>Cảm ơn bạn đã đăng ký tài khoản. Để hoàn tất quá trình đăng ký, vui lòng xác nhận địa chỉ email của bạn bằng cách nhấp vào liên kết dưới đây:</p>\n" +
                "<p><a href=\"" + link + "\">Xác nhận địa chỉ email</a></p>\n" +
                "<p>Nếu bạn không thực hiện thao tác này, tài khoản của bạn sẽ không hoạt động và sẽ bị xóa sau 15 phút.</p>\n";
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            message.setTo(mail);
            message.setSubject("XÁC NHẬN ĐĂNG KÝ TÀI KHOẢN ");
            message.setText(content);
            javaMailSender.send(mimeMessage);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

}
