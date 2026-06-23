package br.com.rivaldo.helpdeskbff.security;

import br.com.rivaldo.models.exceptions.JWTCustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public Claims getClaims(final String token) {
        try {
            // SINTAXE CORRETA PARA A VERSÃO 0.12.5 (Substitui o parserBuilder)
            return Jwts.parser()
                    .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            throw new JWTCustomException(ex.getMessage());
        }
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject() != null ? claims.getSubject() : null;
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> getAuthorities(Claims claims) {
        if(claims.get("authorities") != null) {
            var authorities = (List<LinkedHashMap<String, String>>) claims.get("authorities");

            return authorities.stream()
                    .map(authority -> (GrantedAuthority) () -> authority.get("authority"))
                    .toList();
        }
        throw new JWTCustomException("Invalid token");
    }
}