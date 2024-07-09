package com.caiomorceli.todosimple.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Classe de configuração principal de autenticação

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    // Informa qual é a rota do sistema que é pública (Não precisa de autenticação)
    private static final String[] PUBLIC_MATCHERS = { "/" };

    // Informa qual é a rota do sistema que é pública para POST.
    // O /login nos não implementamos porque ele já é abstrato do Spring. O próprio SpringSecurity cria o /login.
    private static final String[] PUBLIC_MATCHERS_POST = { "/user", "/login" };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpRequest) throws Exception {

        httpRequest.cors().and().csrf().disable();

        // Qualquer requisição autorizada que faça um match de POST será permitida.
        // E qualquer requisição autorizada (GET/POST/PUT/PATCH/DELETE) para /login será permitida.
        // Qualquer outra requisição p/ qualquer outra rota deverá estar autenticado.
        httpRequest.authorizeRequests()
                                    .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                                    .antMatchers(PUBLIC_MATCHERS).permitAll()
                                    .anyRequest().authenticated();

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
