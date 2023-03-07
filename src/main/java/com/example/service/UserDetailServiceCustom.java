package com.example.service;

import com.example.entity.ConfirmTokenRegister;
import com.example.entity.User;
import com.example.entity.UserDetailCustom;
import com.example.exceptionhandle.UserNotEnable;
import com.example.factory.Role;
import com.example.repository.ConfirmTokenRepo;
import com.example.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

//@EnableAsync
@Service
//@Scope("prototype")
@Slf4j
public class UserDetailServiceCustom implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmTokenRepo confirmTokenRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    MailService sendMail;
    @Autowired
    private ConfirmMailService confirmMailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
//        log.warn(user.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("Email " + username + "not found");
        } else if (!user.getEnabled()) {
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
        user.setCreateDate(LocalDateTime.now());
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();


        ConfirmTokenRegister confirmTokenRegister = new ConfirmTokenRegister(token,
                LocalDateTime.now(),
                LocalDateTime.now().plus(15, ChronoUnit.MINUTES),
                user
        );

        confirmTokenRepo.save(confirmTokenRegister);
        sendMail.sendMail(user.getEmail(), token);

        return token;
    }





    public String confirmToken(String token) {


        Optional<ConfirmTokenRegister> tokenRepoOptional = confirmTokenRepo.findByToken(token);


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
