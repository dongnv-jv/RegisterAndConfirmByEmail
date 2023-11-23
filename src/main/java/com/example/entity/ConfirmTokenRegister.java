package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirm_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConfirmTokenRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirm_token_id")
    private Long id;
    @Column(name = "confirm_token_token")
    private String token;
    @Column(name = "confirm_token_created_at")
    private LocalDateTime createdAt;
    @Column(name = "confirm_token_confirmed_at")
    private LocalDateTime confirmedAt;
    @Column(name = "confirm_token_expires_at")
    private LocalDateTime expiresAt;
    @ManyToOne
    @JoinColumn(name = "confirm_token_user")
    private User user;

    public ConfirmTokenRegister(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
