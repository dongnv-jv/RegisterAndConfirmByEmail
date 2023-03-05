package com.example.service;

import com.example.entity.ConfirmToken;
import com.example.entity.User;
import com.example.exceptionhandle.UserNotEnable;
import com.example.factory.Role;
import com.example.repository.ConfirmTokenRepo;
import com.example.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
//@Scope("prototype")
public class UserDetailServiceCustom implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmTokenRepo confirmTokenRepo;
    @Autowired

    private BCryptPasswordEncoder encoder;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    ConfirmMailService confirmMailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Email " + username + "not found");
        }
        else if(!user.getEnabled()){
            try {
                throw new UserNotEnable("User " + username + " is disabled");
            } catch (UserNotEnable e) {
                throw new RuntimeException(e);
            }
        }

        return new UserDetailCustom(user);
    }

//    @Async("asyncExecutor")
    public String registerUser(User user) throws UsernameNotFoundException {

        boolean isExistEmail = (userRepository.findUserByEmail(user.getEmail())) != null;

        if (isExistEmail) {
            throw new UsernameNotFoundException("Email " + user.getEmail() + " is already registered");
        }

        String passwordEncoder = encoder.encode(user.getPassword());

        user.setPassword(passwordEncoder);
        user.setCreateDate(LocalDate.now());
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();


        ConfirmToken confirmToken = new ConfirmToken(token,
                LocalDate.now(),
                LocalDate.now().plus(15, ChronoUnit.DAYS),
                user
        );

        confirmTokenRepo.save(confirmToken);
        sendMail(user.getEmail(), token);

        return token;
    }

//    @Async("asyncExecutor")
    public void sendMail(String mail, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String link = "http://localhost:8080/confirm/" + token;
        String content = "<h1>Xác nhận email</h1>\n" +
                "\t<p>Cảm ơn bạn đã đăng ký tài khoản. Để hoàn tất quá trình đăng ký, vui lòng xác nhận địa chỉ email của bạn bằng cách nhấp vào liên kết dưới đây:</p>\n" +
                "\t<p><a href=\"" + link + "\">Xác nhận địa chỉ email</a></p>\n" +
                "\t<p>Nếu bạn không thực hiện thao tác này, tài khoản của bạn sẽ không hoạt động và sẽ bị xóa sau 15 phút.</p>\n";
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


    public String confirmToken(String token) {


        Optional<ConfirmToken> tokenRepoOptional = confirmTokenRepo.findByToken(token);


        if (!tokenRepoOptional.isPresent()) {

            throw new IllegalStateException("Token not found");


        } else {
            Long id = tokenRepoOptional.get().getUser().getId();
            System.out.println();

            System.out.println(id);
            confirmMailService.confirmMail(id);
            confirmTokenRepo.setConfirmToken(LocalDate.now(), id);
        }


        return "Confirmed";

    }


}
