package edu.utn.UEEDServer.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            ResponseStatusException.class,
    })
    public ResponseEntity<Object> handlerResponseStatusException(ResponseStatusException ex, WebRequest request){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ApiError apiError = new ApiError(ex.getStatus(), ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError,new HttpHeaders(),apiError.getHttpStatus());
    }

    @ExceptionHandler({
            ConstraintViolationException.class
    })

    public ResponseEntity<Object> constraintViolationHandler(ConstraintViolationException exceptions, WebRequest webRequest){
        List<String> errors = new ArrayList<>();

        for(ConstraintViolation ex : exceptions.getConstraintViolations()){
            errors.add(ex.getRootBeanClass() + " " + ex.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exceptions.getLocalizedMessage(),errors);

        return new ResponseEntity<Object>(apiError,new HttpHeaders(), apiError.getHttpStatus());
    }
}
