package com.rajorshi.samay.utils.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * @author ketan.gupta
 */
@Slf4j
public class UserArgumentException extends ClientException {
    private static final long serialVersionUID = 2405997615249959954L;

    public UserArgumentException(String msg, ResponseEntity response) {
        super(msg, response);
    }
}
