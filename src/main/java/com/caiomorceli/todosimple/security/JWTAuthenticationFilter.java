package com.caiomorceli.todosimple.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.caiomorceli.todosimple.model.User;
import com.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;


// Essa classe recebe o que cair na rota /login

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;
    
    private JWTUtil jwtUtil;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.setAuthenticationFailureHandler(new GlobalExceptionHandler()); // chama da classe mãe
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        try{
            // retorna apenas as credenciais do usuário (username e password)
            User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

            // Token que será criado com o usuário
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                                    userCredentials.getUsername(), 
                                                                    userCredentials.getPassword(), new ArrayList<>());

            Authentication authentication = this.authenticationManager.authenticate(authToken);
            return authentication;
        }   catch(IOException ioe){
                throw new RuntimeException(ioe);
            }                
    }


    // Caso a autenticação tenha sucesso. É gerado o token e retornado para o usuário
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, 
                                            FilterChain filterChain, 
                                            Authentication authentication) throws IOException, ServletException{
        
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
        String username = userSpringSecurity.getUsername();

        String token = this.jwtUtil.generateToken(username);

        // retorna na respose para o usuário
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        
    }

}
