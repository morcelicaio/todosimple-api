package com.caiomorceli.todosimple.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiomorceli.todosimple.model.Task;
import com.caiomorceli.todosimple.model.User;
import com.caiomorceli.todosimple.repositorie.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findTaskById(Long id){
        Optional<Task> task = taskRepository.findById(id);

        return task.orElseThrow(() -> new RuntimeException(
            "Tarefa não encontrada! \n Id: "+ id + ", Tipo: "+Task.class.getName()));
    }

    @Transactional
    public Task createTask(Task task){
        User user = this.userService.findById(task.getUser().getId());

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
                throw new RuntimeException("Não é possível excluir pois há entidades relacionadas.");
            }
    }
}
