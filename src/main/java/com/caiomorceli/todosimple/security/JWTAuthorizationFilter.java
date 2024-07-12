package com.caiomorceli.todosimple.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {        

    private JWTUtil jwtUtil;

    // O Spring já entende que essa variável é do tip  o 'UserDetailsServiceImpl'
    private UserDetailsService userDetailsService;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override                               // na request chega o token do usuário
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                                                                    throws IOException, ServletException{
        
        // pega o usuário autenticado e verifica se
        String authorizationHeader = request.getHeader("Authorization");

        if(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer")){
            // remove a string 'bearer' que está junto ao token.
            String token = authorizationHeader.substring(7);

            // Recupera o usuário autenticado com o token dele.
            UsernamePasswordAuthenticationToken authentication = this.getAuthentication(token);

            if(Objects.nonNull(authentication)){  
                // Adiciona no contexto do Spring (Pega o user em qquer parte da aplicação).
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    
    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        if(this.jwtUtil.isValidToken(token)){
            String username = this.jwtUtil.getUsername(token);

            UserDetails user = this.userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, 
                                                                                                            null, 
                                                                                                            user.getAuthorities());

            return authenticatedUser;                                                                                                            
        }

        return null;
    }
}
