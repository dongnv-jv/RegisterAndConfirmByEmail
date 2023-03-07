package com.example.service;

import com.example.repository.ConfirmTokenRepo;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmMailService {
    @Autowired
    private ConfirmTokenRepo confirmTokenRepo;
    @Autowired
    private UserRepository userRepository;
    public String confirmMail(Long id){
      int a=  userRepository.setEnabled(id);
      return a<=0 ? "Confirm failed" : "Confirm successful";
    }




}
