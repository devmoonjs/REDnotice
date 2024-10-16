package io.rednotice.auth.service;


import io.rednotice.auth.request.LoginRequest;
import io.rednotice.auth.request.SignoutRequest;
import io.rednotice.auth.request.SignupRequest;
import io.rednotice.auth.response.SignupResponse;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.common.utils.JwtUtil;

import io.rednotice.user.entity.User;
import io.rednotice.user.enums.UserRole;
import io.rednotice.user.enums.UserStatus;
import io.rednotice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {


    private final BCryptPasswordEncoder passwordEncoders;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Transactional
    public SignupResponse createUser(SignupRequest signupRequest) {

         String encodedPassword = passwordEncoders.encode(signupRequest.getPassword());

        Optional<User> existingUser = userRepository.findByEmail(signupRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new ApiException(ErrorStatus._INVALID_REQUEST);
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encodedPassword,
                UserRole.of(signupRequest.getRole())
        );

        User savedUser = userRepository.save(user);

        return new SignupResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getCreatedAt()
        );

    }

    public String login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->new ApiException(ErrorStatus._NOT_FOUND_USER));

        if(!passwordEncoders.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }

        if(user.getStatus() == UserStatus.WITHDRAWAL){
            throw new ApiException(ErrorStatus._NOT_FOUND_USER);
        }

        return jwtUtil.createToken(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    @Transactional
    public void deleteUser(Long id, SignoutRequest signoutRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->new ApiException(ErrorStatus._NOT_FOUND_USER));

        if(passwordEncoders.matches(signoutRequest.getPassword(), user.getPassword())){
            user.update();
            userRepository.save(user);
        } else throw new ApiException(ErrorStatus._PERMISSION_DENIED);
    }
}



