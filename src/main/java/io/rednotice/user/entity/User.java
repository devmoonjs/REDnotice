package io.rednotice.user.entity;

import io.rednotice.common.Timestamped;
import io.rednotice.member.entity.Member;
import io.rednotice.user.enums.UserRole;
import io.rednotice.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Member> memberList = new ArrayList<>();

    public User(String username, String email, String password, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.userRole = userRole;
        this.password = password;
    }

    public void update() {
        this.status = UserStatus.WITHDRAWAL;
    }
}
