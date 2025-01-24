package com.example.animal_feed.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.animal_feed.config.JwtService;
import com.example.animal_feed.user.Role;
import com.example.animal_feed.user.Users;
import com.example.animal_feed.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<String> register(RegisterRequest request) {
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()));
        var user = userRepository.findByPhone(request.getPhone())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
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

        if (jwtService.isTokenValid(refreshToken) && "REFRESH".equals(jwtService.extractTokenType(refreshToken))) {
            String username = jwtService.extractUsername(refreshToken);
            Optional<Users> userOptional = userRepository.findByPhone(username);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new RefreshTokenResponse(null, "User not found"));
            }
            Users user = userOptional.get();
            // UserDetails userDetails = new User(
            // user.getPhone(),
            // user.getPassword(),
            // List.of(new SimpleGrantedAuthority("ROLE_USER"))
            // );
            String newAccessToken = jwtService.generateToken(user);
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

}
