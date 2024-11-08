package com.example.opombo.auth;

import com.example.opombo.model.entity.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication subject) {

        Instant now = Instant.now();

        long tenHoursInSeconds = 36000L;

        String roles = subject
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Usuario authenticatedUser = (Usuario) subject.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("opombo")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tenHoursInSeconds))
                .subject(subject.getName())
                .claim("roles", roles)
                .claim("userId", authenticatedUser.getId())
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)).getTokenValue();
    }
}