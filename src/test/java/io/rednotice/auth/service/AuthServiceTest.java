package io.rednotice.auth.service;

import io.rednotice.auth.request.LoginRequest;
import io.rednotice.auth.request.SignupRequest;
import io.rednotice.auth.response.SignupResponse;
import io.rednotice.common.exception.ApiException;
import io.rednotice.common.utils.JwtUtil;
import io.rednotice.user.entity.User;
import io.rednotice.user.enums.UserRole;
import io.rednotice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;
    private User user;
    private LoginRequest loginRequest;


    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("testExample@exaple.com", "Qwer!234", "testUSer", "USER");
        user = new User("testUset", "testExample@example.com", "encodedPassword", UserRole.USER);
        user.setId(1L);
        loginRequest = new LoginRequest("testExample.com", "password");
    }

    @Test
    void createUser_Success() {
        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        SignupResponse signupResponse = authService.createUser(signupRequest);

        //then
        assertNotNull(signupResponse);
        assertEquals(user.getId(), signupResponse.getId());
        assertEquals(user.getEmail(), signupResponse.getEmail());
        assertEquals(user.getUsername(), signupResponse.getName());
        assertEquals(user.getCreatedAt(), signupResponse.getCreatedAt());

        verify(userRepository).findByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_UserAlreadyExists() {
        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        //when
        assertThrows(ApiException.class, () -> authService.createUser(signupRequest));
        //then
        verify(userRepository).findByEmail(signupRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).thenReturn("jwtToken");
        //when
        String token = authService.login(loginRequest);
        //then
        assertNotNull(token);
        assertEquals("jwtToken", token);

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil).createToken(user.getId(), user.getEmail(), user.getUserRole());
    }
}