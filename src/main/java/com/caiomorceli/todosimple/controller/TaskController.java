package com.caiomorceli.todosimple.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.caiomorceli.todosimple.model.Task;
import com.caiomorceli.todosimple.model.projection.TaskProjection;
import com.caiomorceli.todosimple.service.TaskService;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    //@Autowired
    //private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable Long id){
        
        Task task = this.taskService.findTaskById(id);

        return ResponseEntity.ok(task);
    }

    /*
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> findAllTasksByUserId(@PathVariable Long userId){
        // Caso seja passado um id de usuário que não existe o servidor retorna um erro.
        this.userService.findUserById(userId);

        List<Task> tasks = this.taskService.findAllTasksByUserId(userId);
        
        return ResponseEntity.ok().body(tasks);
    }           */

    @GetMapping("/user")
    public ResponseEntity<List<TaskProjection>> findAllTasksByUser(){                
        List<TaskProjection> tasks = this.taskService.findAllTasksByUser();
        
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> createTask(@Valid @RequestBody Task task){

        this.taskService.createTask(task);

        // recupera o contexto da requisição e adiciona o @PathVariable com 'buildAndExpand'.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> updateTask(@Valid @RequestBody Task task, @PathVariable Long id){
        task.setId(id);
        this.taskService.updateTask(task);
        

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){

        this.taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }     
}
