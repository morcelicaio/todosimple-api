package com.caiomorceli.todosimple.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiomorceli.todosimple.model.Task;
import com.caiomorceli.todosimple.model.User;
import com.caiomorceli.todosimple.repositorie.TaskRepository;
import com.caiomorceli.todosimple.service.exception.DataBindingViolationException;
import com.caiomorceli.todosimple.service.exception.ObjectNotFoundException;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findTaskById(Long id){
        Optional<Task> task = taskRepository.findById(id);

        return task.orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada! \n Id: "+ id + ", Tipo: "+Task.class.getName()));
    }

    public List<Task> findAllTasksByUserId(Long userId){
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        
        return tasks;
    }

    @Transactional
    public Task createTask(Task task){
        User user = this.userService.findUserById(task.getUser().getId());

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
}
