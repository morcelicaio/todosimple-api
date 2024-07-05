package com.caiomorceli.todosimple.repositorie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caiomorceli.todosimple.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // via função do Spring
    List<Task> findByUser_Id(Long id);

    // via JPQL com passagem de parâmetro
    // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :user_id")
    // List<Task> findByUser_Id(@Param("user_id") Long id);

    // via SQL puro com passagem de parâmetro
    // @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    // List<Task> findByUser_Id(@Param("id") Long id);
}
