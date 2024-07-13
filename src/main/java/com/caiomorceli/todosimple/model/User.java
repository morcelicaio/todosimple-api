package com.caiomorceli.todosimple.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.caiomorceli.todosimple.model.enums.ProfileEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
// @Getter
// @Setter
// @EqualsAndHashCode
@Data       // Substitui de uma vez as anotações @Getter, @Setter  e @EqualsAndHashCode.
@Entity
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 80, nullable = false, unique = true)
    // @NotNull   // Não aceita valor null no atributo.
    // @NotEmpty  // Não aceita string vazia no atributo.
    // Apenas para String, substitui de uma vez as anotações @NotNull e @NotEmpty.
    @NotBlank(message = "O username não pode ser vazio.")
    private String username;

    @Column(name = "password", length = 80, nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY) // Atributo apenas de escrita. A api não retornará esse atributo no json.
    // @NotNull   // Não aceita valor null no atributo.
    // @NotEmpty  // Não aceita string vazia no atributo.
    // Apenas para String, substitui de uma vez as anotações @NotNull e @NotEmpty.
    @NotBlank(message = "O passowrd não pode ser vazio.")
    @Size(min = 4, max = 80, message="A senha deve conter no mínimo 4 e no máximo 80 caracteres.")
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonProperty(access = Access.WRITE_ONLY)     // Atributo apenas de escrita. Não é retornado no JSON no momento da leitura.
    private List<Task> tasks = new ArrayList<>(); 
    
    @ElementCollection(fetch = FetchType.EAGER)   // Será uma tabela no BD que é uma coleção de perfis associados à tabela de usuário. FetchType.EAGER: Sempre que buscar os usuários, os perfis também são retornados 
    @JsonProperty(access = Access.WRITE_ONLY)     // Atributo apenas de escrita. A api não retornará esse atributo no json.
    @CollectionTable(name = "user_profile")       // Define o nome da tabela no BD.
    @Column(name = "profile", nullable = false)   // Define o nome do atributo na tabela.
    private Set<Integer> profiles = new HashSet<>();


    // Retorna a lista com os ENUMS 
    public Set<ProfileEnum> getProfiles(){
        return this.profiles.stream().map(profile -> ProfileEnum.toEnum(profile)).collect(Collectors.toSet());
    }

    // Adiciona um novo código à lista de perfis.
    public void addProfile(ProfileEnum profileEnum){
        this.profiles.add(profileEnum.getCode());
    }

}
