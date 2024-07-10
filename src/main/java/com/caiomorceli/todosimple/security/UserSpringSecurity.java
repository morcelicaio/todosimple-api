package com.caiomorceli.todosimple.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.caiomorceli.todosimple.model.enums.ProfileEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

// O UserSpringSecurity também é conhecido no Spring como UserDetails.
// No caso são os detalhes que são necessários no sistema (atributos e métodos necessários p/ conseguir instanciar o usuário no
// sistema de autenticação). É diferente o objeto User porque ele tem apenas as informações de autenticação.
// No caso são username, password e as autoridades (que são os perfis).

@NoArgsConstructor
@Getter
public class UserSpringSecurity implements UserDetails {
    
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public UserSpringSecurity(Long id, String username, String password, Set<ProfileEnum> profileEnums) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = profileEnums.stream().map(pe -> new SimpleGrantedAuthority(pe.getDescription())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;        
    }

    @Override
    public boolean isAccountNonLocked() {        
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {        
        return true;
    }

    public boolean hasRole(ProfileEnum profileEnum){
        
        // Verifica se na lista possui alguma das autoridades e retorna true ou false.
        return this.getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    }
    
}
