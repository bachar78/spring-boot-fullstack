package com.bachar.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class JWTUtil {
    private static final String SECRET_KEY =
            "bachar_19781978_dawod19781978_bachar_19781978_dawod19781978_bachar_19781978_dawod19781978";

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String... scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, List<String> scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }
    public String issueToken(
            String subject,
            Map<String, Object> claims) {
        return Jwts.builder()
                .issuer("https://github.com/bachar78")
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(Instant.now().plus(15, DAYS))
                )
                .signWith(getSigningKey())
                .compact();
    }

    //Method that allows to subtract the subject from the token(the email in our case which is unique)
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean isTokenValid(String jwt, String username) {
        String jwtSubject = getSubject(jwt);
        return jwtSubject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }
}
