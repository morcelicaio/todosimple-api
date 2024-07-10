package com.caiomorceli.todosimple.model.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileEnum {
    
    // É necessário ser definido exatamente dessa forma para o Spring compreender que isso é um perfil.
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    
    public static ProfileEnum toEnum(Integer code){
        if(Objects.isNull(code)){
            return null;    
        }
                
        for (ProfileEnum pe : ProfileEnum.values()) {
            if(code.equals(pe.getCode())){
                return pe;
            }
        }

        // Caso seja passado um perfil que não existe.
        throw new IllegalArgumentException("Código inválido: "+ code);
    }

}
