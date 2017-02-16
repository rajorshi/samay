package com.rajorshi.samay.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationError {

    int line ;
    List<String> errorMessages ;

    public ValidationError(int rowNumber) {
        this.line = rowNumber;
        this.errorMessages = new ArrayList<>();
    }

    public ValidationError(int rowNumber ,List<String> errorMessages) {
        this.line = rowNumber;
        this.errorMessages = errorMessages;
    }
}
