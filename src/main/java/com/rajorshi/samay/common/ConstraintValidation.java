package com.rajorshi.samay.common;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ankita.dhyani
 */
public class ConstraintValidation<E> {



    public void addToValidationError(Validator validator , ValidationError error, E  e) {
        error.getErrorMessages().addAll(getValidationErrorMessages(validator,e));
    }

    public List<String> getValidationErrorMessages(Validator validator , E  e) {
        List<String> errorMessages = new ArrayList<>();
        Set<ConstraintViolation<E>> validate = validator.validate(e);
        validate.forEach( violation -> {
            errorMessages.add(violation.getPropertyPath().toString() +":"+ violation.getMessage());
        });
        return errorMessages;
    }

}
