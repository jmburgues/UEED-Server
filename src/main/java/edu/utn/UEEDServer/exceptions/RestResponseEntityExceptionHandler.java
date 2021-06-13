package edu.utn.UEEDServer.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            IDnotFoundException.class
    })

    public ResponseEntity<Object> idNotFoundExceptionHandler(IDnotFoundException exceptions){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, exceptions.getLocalizedMessage(),List.of(exceptions.getMessage()));

        return new ResponseEntity<Object>(apiError,new HttpHeaders(), apiError.getHttpStatus());
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

    @ExceptionHandler({
            IllegalArgumentException.class
    })

    public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException exceptions){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exceptions.getLocalizedMessage(),List.of(exceptions.getMessage()));

        return new ResponseEntity<Object>(apiError,new HttpHeaders(), apiError.getHttpStatus());
    }
}
