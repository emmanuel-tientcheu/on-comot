package com.example.comot.auth.application.services.jwtService;

import com.example.comot.auth.domaine.model.AuthUser;
import com.example.comot.auth.domaine.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ConcreteJWTService implements JWTService{

    private final SecretKey secretKey;
    private final long expiration;

    public ConcreteJWTService(String secret, long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    @Override
    public String tokenize(User user) {
       var claims =  Jwts
                .claims()
                .subject(user.getId())
                .add("first_name", user.getFirstName())
                .add("last_name", user.getLastname())
                .add("email", user.getEmail())
                .build();

       var createdAt = LocalDateTime.now();
       var expirationAt = createdAt.plusSeconds(this.expiration);

       return Jwts
               .builder()
               .claims(claims)
               .issuedAt(
                       Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant())
               )
               .expiration(
                       Date.from(expirationAt.atZone(ZoneId.systemDefault()).toInstant())
               )
               .signWith(this.secretKey)
               .compact();
    }

    @Override
    public AuthUser parse(String token) {
        var jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
        var claims = jwtParser.parseSignedClaims(token).getPayload();

        return new AuthUser(
                claims.getSubject(),
                claims.get("first_name", String.class),
                claims.get("last_name", String.class),
                claims.get("email", String.class)
        );
    }
}
