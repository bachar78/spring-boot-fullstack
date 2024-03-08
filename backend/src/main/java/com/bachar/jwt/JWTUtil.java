package com.bachar.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class JWTUtil {
    private static final String SECRET_KEY =
            "bachar_19781978_dawod19781978_bachar_19781978_dawod19781978_bachar_19781978_dawod19781978";

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(
            String subject,
            Map<String, Object> claims) {
//        MacAlgorithm alg = Jwts.SIG.HS256;
//        SecretKey key = alg.key().build();
        return Jwts.builder()
                .issuer("https://github.com/bachar78")
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(Instant.now().plus(15, DAYS))
                )
                //.signWith(key, alg)
                .signWith(getSigningKey())
                .compact();
    }


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
