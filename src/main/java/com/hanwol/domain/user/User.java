package com.hanwol.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender; // 로그인 후 최초 캐릭터 설정 시 세팅됨

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private long exp = 0;

    @Column(nullable = false)
    private long gold = 0;

    @Column(nullable = false)
    private long premiumCurrency = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void selectGender(Gender gender) {
        if (this.gender != null) {
            throw new IllegalStateException("성별은 한 번만 선택할 수 있습니다.");
        }
        this.gender = gender;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
