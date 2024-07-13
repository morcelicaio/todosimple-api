package com.caiomorceli.todosimple.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que cria um usuário

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {
    
    @NotBlank
    @Size(max = 80)
    private String username;

    @NotBlank
    @Size(min = 4, max = 80)
    private String password;
}
