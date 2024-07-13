package com.caiomorceli.todosimple.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiomorceli.todosimple.model.Task;
import com.caiomorceli.todosimple.model.User;
import com.caiomorceli.todosimple.model.enums.ProfileEnum;
import com.caiomorceli.todosimple.model.projection.TaskProjection;
import com.caiomorceli.todosimple.repository.TaskRepository;
import com.caiomorceli.todosimple.security.UserSpringSecurity;
import com.caiomorceli.todosimple.service.exception.AuthorizationException;
import com.caiomorceli.todosimple.service.exception.DataBindingViolationException;
import com.caiomorceli.todosimple.service.exception.ObjectNotFoundException;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    /*
    public Task findTaskById(Long id){
        Optional<Task> task = taskRepository.findById(id);

        return task.orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada! \n Id: "+ id + ", Tipo: "+Task.class.getName()));
    }   */
    public Task findTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada! - Id: "+ id + ", Tipo: "+Task.class.getName()));

        // Recupera o usuário que está no contexto da aplicação (está logado).        
        UserSpringSecurity userSpringSecurity = UserService.authenticated();

        // Se o usuário não está logado ou se não tem perfil de adm e a task não pertence a ele é disparada a exception.
        if(Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !this.userHasTask(userSpringSecurity, task)){

            throw new AuthorizationException("Acesso negado!");
        }        

        return task;
    }

    /*
    public List<Task> findAllTasksByUserId(Long userId){
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        
        return tasks;
    }       */

    // Busca apenas do usuário que está logado e não mais pelo id.
    public List<TaskProjection> findAllTasksByUser(){
        // Recupera o usuário que está no contexto da aplicação (está logado).        
        UserSpringSecurity userSpringSecurity = UserService.authenticated();

        // Se o usuário não está logado é disparada a exception.
        if(Objects.isNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso negado!");
        }

        // Utiliza o id do usuário que está logado para fazer a busca.
        List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        
        return tasks;
    }

    /*
    @Transactional
    public Task createTask(Task task){
        User user = this.userService.findUserById(task.getUser().getId());

        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);

        return task;
    }       */

    @Transactional
    public Task createTask(Task task){
        // Recupera o usuário que está no contexto da aplicação (está logado).        
        UserSpringSecurity userSpringSecurity = UserService.authenticated();

        // Se o usuário não está logado é disparada a exception.
        if(Objects.isNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso negado!");
        }

        // Utiliza o id do usuário que está logado para fazer a busca.
        User user = this.userService.findUserById(userSpringSecurity.getId());

        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);

        return task;
    }

    @Transactional
    public Task updateTask(Task task){
        Task newTask = this.findTaskById(task.getId());

        newTask.setDescription(task.getDescription());

        return this.taskRepository.save(newTask);
    }

    public void deleteTask(Long id){
        Task task = this.findTaskById(id);

        try{
            this.taskRepository.delete(task);
        }   catch(Exception e){
                // Não exclui caso a task tenha outras entidades relacionadas a ela.
                throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas.");
            }
    }

    // Verifica se o id do dono da task é o mesmo do usuário que está logado. Se o usuário é dono dessa task.
    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){        
        return task.getUser().getId().equals(userSpringSecurity.getId());                
    }
}
