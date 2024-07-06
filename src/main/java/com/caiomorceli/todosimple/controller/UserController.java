package com.caiomorceli.todosimple.controller;

import java.net.URI;

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

import com.caiomorceli.todosimple.model.User;
import com.caiomorceli.todosimple.model.User.CreateUser;
import com.caiomorceli.todosimple.model.User.UpdateUser;
import com.caiomorceli.todosimple.service.UserService;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;

    // Ex: http://localhost:8080/user/1
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = this.userService.findById(id);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @Validated(CreateUser.class)
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user){
        this.userService.createUser(user);

        // recupera o contexto da requisição e adiciona o @PathVariable com 'buildAndExpand'.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> updateUser(@Valid @RequestBody User user, @PathVariable Long id){
        user.setId(id);

        this.userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        this.userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
