package com.caiomorceli.todosimple.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.caiomorceli.todosimple.security.JWTAuthenticationFilter;
import com.caiomorceli.todosimple.security.JWTAuthorizationFilter;
import com.caiomorceli.todosimple.security.JWTUtil;

// Classe de configuração principal de autenticação

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;
    
    // Informa qual é a rota do sistema que é pública (Não precisa de autenticação)
    private static final String[] PUBLIC_MATCHERS = { "/" };

    // Informa qual é a rota do sistema que é pública para POST.
    // O /login nos não implementamos porque ele já é abstrato do Spring. O próprio SpringSecurity cria o /login.
    private static final String[] PUBLIC_MATCHERS_POST = { "/user", "/login" };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpRequest) throws Exception {

        httpRequest.cors().and().csrf().disable();

        AuthenticationManagerBuilder authenticationManagerBuilder = httpRequest
            .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(this.bCryptPasswordEncoder());
        this.authenticationManager = authenticationManagerBuilder.build();

        // Qualquer requisição autorizada que faça um match de POST será permitida.
        // E qualquer requisição autorizada (GET/POST/PUT/PATCH/DELETE) para /login será permitida.
        // Qualquer outra requisição p/ qualquer outra rota deverá estar autenticado.
        httpRequest.authorizeRequests()
                                    .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                                    .antMatchers(PUBLIC_MATCHERS).permitAll()
                                    .anyRequest().authenticated().and()
                                    .authenticationManager(authenticationManager);

        httpRequest.addFilter(new JWTAuthenticationFilter(this.authenticationManager, this.jwtUtil));
        httpRequest.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.jwtUtil, this.userDetailsService));

        // A política de sessão é de não salvar a sessão. Sem estado (STATELESS).
        httpRequest.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpRequest.build();
    }


    // Configuração de cors do spring security
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        // Aplica a configuração default de cors.
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        // Libera os métodos http.
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));

        // Cria o source passando as configurações feitas acima.
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        // O BCryptEncoder serve para criptografar (quando o usuário é criado sua senha é criptografada)
        return new BCryptPasswordEncoder();
    }
}
