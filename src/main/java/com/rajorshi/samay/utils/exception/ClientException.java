package com.rajorshi.samay.utils.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * @author ketan.gupta
 */
@Slf4j
public class ClientException extends RuntimeException {
    private static final long serialVersionUID = 2405997615249959954L;

    @Getter
    private ResponseEntity response;

    public ClientException(String msg, ResponseEntity response) {
        super(msg);
        this.response = response;
        log.error(msg);
    }
}
