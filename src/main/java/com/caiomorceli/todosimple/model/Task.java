package com.caiomorceli.todosimple.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
// @Getter
// @Setter
// @EqualsAndHashCode
@Data       // Substitui de uma vez as anotações @Getter, @Setter  e @EqualsAndHashCode.
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(name = "description", length = 250, nullable = false)
    // @NotNull   // Não aceita valor null no atributo.
    // @NotEmpty  // Não aceita string vazia no atributo.
    @NotBlank   // Apenas para String, substitui de uma vez as anotações @NotNull e @NotEmpty.
    @Size(min = 1, max = 250)
    private String description;
}
