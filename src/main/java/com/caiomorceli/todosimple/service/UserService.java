package com.caiomorceli.todosimple.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiomorceli.todosimple.model.User;
import com.caiomorceli.todosimple.repositorie.TaskRepository;
import com.caiomorceli.todosimple.repositorie.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
   
    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        
        return user.orElseThrow(() -> new RuntimeException(
            "Usuário não encontrado! \n Id: " + id + ", \n Tipo: " + User.class.getName()
        ));
    }

    @Transactional
    public User createUser(User user){
        user.setId(null);

        user = this.userRepository.save(user);

        // Caso o usuário seja criado e já sejam atribuídas algumas tasks a ele, pode-se já armazenar as tasks junto.
        this.taskRepository.saveAll(user.getTasks());

        return user;
    }

    @Transactional
    public User updateUser(User user){
        User newUser = this.findById(user.getId());

        newUser.setPassword(user.getPassword());

        return this.userRepository.save(newUser);
    }


    public void deleteUser(Long id){
        User user = findById(id);

        try{
            this.userRepository.delete(user);
        }   catch(Exception e){
                // Não exclui caso o usuário tenha tarefas relacionadas a ele.
                throw new RuntimeException("Não é possível excluir pois há entidades (tarefas) relacionadas.");
            }
    }
}
