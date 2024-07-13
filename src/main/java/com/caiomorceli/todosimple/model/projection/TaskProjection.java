package com.caiomorceli.todosimple.model.projection;

// Tudo no repository pode ser retornado como interface.

// As projections sao interfaces criadas p/ pegar os atributos de queries nativas. 
// Elas serao o espelho do nome do campo que voce atribui na query nativa.

public interface TaskProjection {

    public Long getId();

    public String getDescription();

    // Não retorna o atributo user_id
    
}
