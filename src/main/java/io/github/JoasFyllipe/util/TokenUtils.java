package io.github.JoasFyllipe.util;

import io.smallrye.jwt.build.Jwt;
import java.time.Duration;
import java.util.Set;

public class TokenUtils {
    public static String generateToken(String email, Long userId, Set<String> roles) {
        return Jwt.issuer("unitins-jwt") // Mesmo issuer do seu application.properties
                .upn(email)
                .subject(userId.toString())
                .groups(roles)
                .expiresIn(Duration.ofHours(1))
                .sign();
    }
}