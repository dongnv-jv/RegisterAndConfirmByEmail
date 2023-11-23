package com.example.repository;

import com.example.entity.ConfirmTokenRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmTokenRepo extends JpaRepository<ConfirmTokenRegister, Long> {


    Optional<ConfirmTokenRegister> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmTokenRegister u SET u.confirmedAt=?1  WHERE u.user.id=?2")
    int setConfirmToken(LocalDateTime date, Long id);

}
