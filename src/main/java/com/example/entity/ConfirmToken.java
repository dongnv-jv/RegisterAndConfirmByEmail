package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "confirm_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConfirmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirm_token_id")
    private Long id;
    @Column(name = "confirm_token_token")
    private String token;
    @Column(name = "confirm_token_created_at")
    private LocalDate createdAt;
    @Column(name = "confirm_token_confirmed_at")
    private LocalDate confirmedAt;
    @Column(name = "confirm_token_expires_at")
    private LocalDate expiresAt;
    @ManyToOne
    @JoinColumn(name = "confirm_token_user")
    private User user;

    public ConfirmToken(String token, LocalDate createdAt, LocalDate expiresAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
