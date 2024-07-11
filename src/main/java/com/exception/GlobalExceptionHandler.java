package com.exception;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.caiomorceli.todosimple.service.exception.DataBindingViolationException;
import com.caiomorceli.todosimple.service.exception.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;


@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER") // Annotation do lombok que é um logger que imprime no console as anotações dessa classe.
@RestControllerAdvice               // Annotation que avisa o Spring que é uma classe que deve ser inicializa junto do Spring.
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationFailureHandler {
    
    // @Value busca o valor do arquivo application.properties
    @Value("${server.error.include-exception}")
    private boolean printStackTrace;


    // Método que captura qualquer tipo de exceção do sistema que seja um argumento inválido. Ex: (Criar usuário sem senha)
    // Erro 422.
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação. Verifique o campo 'errors' para detalhes.");

        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }


    // Método que captura qualquer exceção que não foi tratada
    // Erro 500.
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        final String errorMessage = "Ocorreu um erro desconhecido";

        log.error(errorMessage, exception);     // @Slf4j  captura esse log.
        
        return buildErrorResponse(exception, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    // Método que captura qualquer tipo de exceção do sistema que seja um erro de integridade. Ex: (Criar usuário com username igual)
    // Erro 409.
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
                                                            DataIntegrityViolationException dataIntegrityViolationException,
                                                            WebRequest request) {
        
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Erro ao salvar. Entidade com problema de integridade: " + errorMessage, dataIntegrityViolationException);
        
        return buildErrorResponse(dataIntegrityViolationException, errorMessage, HttpStatus.CONFLICT, request);
    }


    // Erro 422
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException constraintViolationException,
                                                                    WebRequest request) {
        
        log.error("Falhou ao validar o elemento", constraintViolationException);
        
        return buildErrorResponse(constraintViolationException, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }


    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException objectNotFoundException,
                                                                WebRequest request) {
        
            log.error("Falhou ao buscar o elemento solicitado", objectNotFoundException);
        
        return buildErrorResponse(objectNotFoundException, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(DataBindingViolationException dataBindingViolationException,
                                                                    WebRequest request) {
        
        log.error("Failed to save entity with associated data", dataBindingViolationException);
        
        return buildErrorResponse(dataBindingViolationException, HttpStatus.CONFLICT, request);
    }



    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }


    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, 
                                                        WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        Integer status = HttpStatus.FORBIDDEN.value();

        // Irá retornar o erro 401
        response.setStatus(status);
        response.setContentType("application/json");
        
        ErrorResponse errorResponse = new ErrorResponse(status, "E-mail ou senha inválidos");
        response.getWriter().append(errorResponse.toJson());
    }

}
