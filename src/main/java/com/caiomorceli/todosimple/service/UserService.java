package com.caiomorceli.todosimple.service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiomorceli.todosimple.model.User;
import com.caiomorceli.todosimple.model.enums.ProfileEnum;
import com.caiomorceli.todosimple.repositorie.UserRepository;
import com.caiomorceli.todosimple.security.UserSpringSecurity;
import com.caiomorceli.todosimple.service.exception.AuthorizationException;
import com.caiomorceli.todosimple.service.exception.DataBindingViolationException;
import com.caiomorceli.todosimple.service.exception.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private UserRepository userRepository;
   
    public User findUserById(Long id){        
        // Recupera o usuário que está nesse momento no contexto da aplicação.
        UserSpringSecurity userSpringSecurity = authenticated();
        
        // verifica se o usuário está logado ou se não é um usuário com perfil de administrador e se não está buscando pelo seu próprio ID.
        if(!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())){  
            // Se não estiver, lança a exceção de acesso negado.
            throw new AuthorizationException("Acesso negado!");
        }

        Optional<User> user = this.userRepository.findById(id);
        
        return user.orElseThrow(() -> new ObjectNotFoundException(
            "Usuário não encontrado! \n Id: " + id + ", \n Tipo: " + User.class.getName()
        ));
    }

    @Transactional
    public User createUser(User user){
        user.setId(null);

        // Criptografa a senha do usuário que será salvo.
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        // Define que a criação do usuário terá o perfil de usuário (perfil 2) e não de admin.
        // Isso evita que um usuário mal intencionado cadastre um usuário com perfil de admin. 
        user.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        user = this.userRepository.save(user);        

        return user;
    }

    @Transactional
    public User updateUser(User user){
        User newUser = this.findUserById(user.getId());

        // Criptografa a senha do usuário que será atualizado.
        newUser.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        return this.userRepository.save(newUser);
    }


    public void deleteUser(Long id){
        User user = findUserById(id);

        try{
            this.userRepository.delete(user);
        }   catch(Exception e){
                // Não exclui caso o usuário tenha tarefas relacionadas a ele.
                throw new DataBindingViolationException("Não é possível excluir pois há entidades (tarefas) relacionadas.");
            }
    }

    // O UserSpringSecurity é o usuário que está no contexto atual do Spring (É quem está logado no momento).
    // Com o token passado é possível pegar no contexto quem está autenticado.
    public static UserSpringSecurity authenticated(){
        try{
            UserSpringSecurity userSpringSecurity = (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userSpringSecurity;
        }   catch(Exception e){                
                return null;
        }
    }
}
