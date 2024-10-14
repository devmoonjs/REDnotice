package io.rednotice.user.entity;

import io.rednotice.common.Timestamped;
import io.rednotice.user.enums.UserRole;
import io.rednotice.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    public User(String username, String email, String password, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.userRole = userRole != null ? userRole : UserRole.USER;
        this.password = password;


    }

    public void update() {
        this.status = UserStatus.WITHDRAWAL;
    }
}
