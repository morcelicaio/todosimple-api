package com.caiomorceli.todosimple.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO que atualiza um usuário  (na atualização do usuário é permitido apenas alterar o password)
// username é um campo 'unique' no BD. Não é alterado.

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {
    
    private Long id;

    @NotBlank
    @Size(min = 4, max = 80)
    private String password;
}
