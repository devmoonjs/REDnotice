package io.rednotice.auth.service;


import io.rednotice.auth.request.LoginRequest;
import io.rednotice.auth.request.SignoutRequest;
import io.rednotice.auth.request.SignupRequest;
import io.rednotice.auth.response.SignupResponse;
import io.rednotice.config.JwtUtil;
import io.rednotice.config.PasswordEncoders;
import io.rednotice.user.entity.User;
import io.rednotice.user.enums.UserRole;
import io.rednotice.user.enums.UserStatus;
import io.rednotice.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoders passwordEncoders;
    private final JwtUtil jwtUtil;


    @Transactional
    public SignupResponse createUser(@Valid SignupRequest signupRequest) {

        String encodedPassword = passwordEncoders.encode(signupRequest.getPassword());

        Optional<User> existingUser = userRepository.findByEmail(signupRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserRole userRole = signupRequest.getRole() != null ? signupRequest.getRole() : UserRole.USER;

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );

        User savedUser = userRepository.save(user);

        return new SignupResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );

    }

    public String login(@Valid LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->new NoSuchElementException("User not found"));

        if(!passwordEncoders.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        if(user.getStatus() == UserStatus.WITHDRAWAL){
            throw new IllegalArgumentException("User is withdrawal");
        }

        return jwtUtil.createToken(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );
    }

//    public void deleteUser(Long id, SignoutRequest signoutRequest) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() ->new NoSuchElementException("User not found"));
//
//        if(passwordEncoders.matches(signoutRequest.getPassword(), user.getPassword())){
//            user.update();
//        } else throw new IllegalArgumentException("Wrong password");
//    }
}



