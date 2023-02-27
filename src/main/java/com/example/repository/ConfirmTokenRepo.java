package com.example.repository;

import com.example.entity.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

public interface ConfirmTokenRepo extends JpaRepository<ConfirmToken,Long> {


    Optional<ConfirmToken> findByToken(String token);
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmToken u SET u.confirmedAt=?1  WHERE u.user.id=?2")
    int setConfirmToken(LocalDate date,Long id);

}
