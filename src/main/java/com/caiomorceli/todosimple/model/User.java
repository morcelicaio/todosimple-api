package com.caiomorceli.todosimple.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "user")
public class User {

    public interface CreateUser{ 

    }

    public interface UpdateUser{

    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class)  // Não aceita valor null no atributo.
    @NotEmpty(groups = CreateUser.class) // Não aceita string vazia no atributo.
    private String username;

    @Column(name = "password", length = 50, nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY) // Atributo apenas de escrita. A api não retornará esse atributo no json.
    @NotNull(groups = { CreateUser.class, UpdateUser.class })
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class })
    @Size(groups = { CreateUser.class, UpdateUser.class }, min = 4, max = 50, message="A senha deve conter no mínimo 4 e no máximo 50 caracteres.") // O password deve ter no mínimo 7 e no máximo 50 caracteres.
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonProperty(access = Access.WRITE_ONLY)     // Atributo apenas de escrita. Não é retornado no JSON no momento da leitura.
    private List<Task> tasks = new ArrayList<>();                
}
