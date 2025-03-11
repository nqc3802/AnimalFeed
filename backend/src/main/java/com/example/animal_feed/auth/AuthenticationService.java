package com.example.animal_feed.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.animal_feed.config.JwtService;
import com.example.animal_feed.exception.InvalidLoginException;
import com.example.animal_feed.user.CustomUserDetails;
import com.example.animal_feed.user.Role;
import com.example.animal_feed.user.Users;

import jakarta.transaction.Transactional;

import com.example.animal_feed.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<String> register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists");
        }
        var user = Users.builder()
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .email(request.getEmail())
                .address(request.getAddress())
                .role(Role.USER)
                .state("ACTIVE")
                .build();
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getPhone(),
                            request.getPassword()));
        } catch (AuthenticationException e) {
            throw new InvalidLoginException("Invalid phone number or password");
        }
        var user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(
                        () -> new InvalidLoginException("Invalid phone number or password"));
        var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        RefreshToken tokenEntity = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(tokenEntity);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RefreshTokenResponse(null, "Refresh token is required"));
        }

        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(refreshToken);
        if (storedToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RefreshTokenResponse(null, "Invalid refresh token"));
        }

        if (jwtService.isTokenValid(refreshToken) && "REFRESH".equals(jwtService.extractTokenType(refreshToken))) {
            String username = jwtService.extractUsername(refreshToken);
            Optional<Users> userOptional = userRepository.findByPhone(username);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new RefreshTokenResponse(null, "User not found"));
            }
            Users user = userOptional.get();
            var userDetails = new CustomUserDetails(user);
            String newAccessToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(
                    RefreshTokenResponse.builder()
                            .token(newAccessToken)
                            .message("Refresh token success")
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RefreshTokenResponse(null, "Invalid refresh token"));
        }
    }

    @Transactional
    public ResponseEntity<String> logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
        return ResponseEntity.ok("Logout successful");
    }

}
