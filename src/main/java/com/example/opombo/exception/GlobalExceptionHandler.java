package com.example.opombo.exception;

import java.util.HashMap;
import java.util.Map;

import com.example.opombo.exception.PomboException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manipula todas as exceções do tipo PomboException
    @ExceptionHandler(PomboException.class)
    public ResponseEntity<String> tratarPomboException(PomboException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarExcecoesDeValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();

        // Itera sobre os erros de campo e coleta as mensagens
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nomeCampo = ((FieldError) error).getField();
            String mensagemErro = error.getDefaultMessage();
            erros.put(nomeCampo, mensagemErro);
        });

        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }
}