package com.example.entity;

import com.example.factory.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_control")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_created_at")
    private LocalDateTime createDate;
    @Column(name = "user_updated_at")
    private LocalDateTime updateDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean locked = false;
    private Boolean enabled = false;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
