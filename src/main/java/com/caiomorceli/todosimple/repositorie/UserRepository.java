package com.caiomorceli.todosimple.repositorie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caiomorceli.todosimple.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    public User findUserByUsername(String username);        
}
