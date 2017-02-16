package com.rajorshi.samay.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler
{
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity defaultErrorHandler(HttpServletRequest request, Exception exception) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (exception.getClass(), ResponseStatus.class) != null)
            throw exception;

        log.error("Exception", exception);
        StringWriter errorMessage = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorMessage));
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage.toString());


    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity InValidRequestHandler(HttpServletRequest request, MethodArgumentNotValidException exception) throws Exception {
        log.error("Exception", exception);
        List<FieldError> jsonValidationErrors =  exception.getBindingResult().getFieldErrors();
        ArrayList<String> errors = new ArrayList<>();
        jsonValidationErrors.forEach(fieldError ->
        {
            errors.add(fieldError.getField()+ ":" + fieldError.getDefaultMessage());
        });
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
