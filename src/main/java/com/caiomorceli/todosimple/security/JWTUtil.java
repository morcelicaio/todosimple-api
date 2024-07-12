package com.caiomorceli.todosimple.security;

import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;
    
    // Gera o token.
    public String generateToken(String username){
        SecretKey secretKey = this.getKeyBySecret();
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
            .signWith(secretKey)
            .compact();        
    }    
    
    private SecretKey getKeyBySecret(){
        SecretKey secretKey = Keys.hmacShaKeyFor(this.secret.getBytes());
        return secretKey; // a key é gerada para poder gerar o token de autenticação.
    }

    // Verifica se o token é valido na hora da autorização.
    public boolean isValidToken(String token){
        Claims claims = this.getClaims(token);
        if(Objects.nonNull(claims)){
            String username = claims.getSubject(); // subject é o username do usuário
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());

            // verifica se o token é válido verificando se o username não é nulo 
            // e a data de expiração do token deve ser maior do que a data atual (now).
            if(Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate)){
                return true;
            }            
        }

        return false;
    }

    public String getUsername(String token){        
        Claims claims = this.getClaims(token);

        if(Objects.nonNull(claims)){    // Token é valido
            return claims.getSubject();
        }

        return null;
    }

    // Transforma o token nos dados.
    // O Claim contém as informações relevantes de autenticação ou autorização.
    private Claims getClaims(String token){
        SecretKey secretKey = this.getKeyBySecret();

        try{
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        }   catch(Exception e){
                // se não conseguir gerar os dados do claims
                return null;
        }
    }
}
