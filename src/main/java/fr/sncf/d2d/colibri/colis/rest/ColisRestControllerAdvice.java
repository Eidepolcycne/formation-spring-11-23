package fr.sncf.d2d.colibri.colis.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.sncf.d2d.colibri.users.exceptions.UserNotFoundException;

@RestControllerAdvice(assignableTypes = { ColisRestController.class })
public class ColisRestControllerAdvice {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handeUserNotFound(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(exception.getMessage());
    }
}
