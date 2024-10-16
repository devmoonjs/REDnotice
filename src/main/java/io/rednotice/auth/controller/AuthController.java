package io.rednotice.auth.controller;

import io.rednotice.auth.request.LoginRequest;
import io.rednotice.auth.request.SignoutRequest;
import io.rednotice.auth.request.SignupRequest;
import io.rednotice.auth.response.SignupResponse;
import io.rednotice.auth.service.AuthService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/signup")
    public ApiResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return ApiResponse.ok(authService.createUser(signupRequest));
    }

    @PostMapping("/v1/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",authService.login(loginRequest)).build();
    }

    @PutMapping("/v1/signout")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody SignoutRequest signoutRequest) {
        Long id = authUser.getId();
        authService.deleteUser(id, signoutRequest);
        return ResponseEntity.noContent().build();
    }
}
