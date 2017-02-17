package com.rajorshi.samay.service.impl;

import com.rajorshi.samay.common.ValidationError;
import com.rajorshi.samay.model.dao.CallbackRequestDao;
import com.rajorshi.samay.model.dao.CallbackRequestSearchFilter;
import com.rajorshi.samay.model.repository.CallbackMethod;
import com.rajorshi.samay.model.repository.CallbackRequest;
import com.rajorshi.samay.resources.dto.NewCallbackRequestDto;
import com.rajorshi.samay.resources.dto.SavedCallbackRequestDto;
import com.rajorshi.samay.service.CallbackService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Validator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class CallbackServiceImpl implements CallbackService {

    @Inject
    private Validator validator;
    @Inject
    private CallbackRequestDao callbackDao;

    @Override
    @Transactional
    public ResponseEntity registerCallback(NewCallbackRequestDto dto) {

        try {
            return ResponseEntity.ok(toDto(callbackDao.createNewTimedCallback(fromDto(dto))));
        } catch (URISyntaxException | IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Collections.singletonList(
                                    new ValidationError(0, Collections.singletonList(e.getMessage()))
                            )
                    );
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Collections.singletonList(
                                    new ValidationError(0, Collections.singletonList(e.getMessage()))
                            )
                    );
        }
    }

    @Override
    public ResponseEntity findCallbacks(CallbackRequestSearchFilter filter, int offset, int limit) {

        List<SavedCallbackRequestDto> dtos = new ArrayList<>();
        List<CallbackRequest> callbacks = Collections.emptyList();

        try {
            callbackDao.findCallbacks(filter, offset, limit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Collections.singletonList(
                                    new ValidationError(0, Collections.singletonList(e.getMessage()))
                            )
                    );
        }
        for (CallbackRequest callback: callbacks) {
            try {
                dtos.add(toDto(callback));
            } catch (URISyntaxException e) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                Collections.singletonList(
                                        new ValidationError(0, Collections.singletonList(e.getMessage()))
                                )
                        );
            }
        }
        return ResponseEntity.ok(dtos);
    }

    private CallbackRequest fromDto(NewCallbackRequestDto dto)
            throws IllegalArgumentException, URISyntaxException {

        URI uri = new URI(dto.getTargetUrl());
        CallbackMethod.fromString(uri.getScheme()); // forcing parse to check correctness
        Date callAt = dto.getAt();
        if (callAt == null) {
            if (dto.getAfter() <= 0) {
                throw new IllegalArgumentException("Illegal or null callback time specified.");
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, dto.getAfter());
            callAt = calendar.getTime();
        }
        return CallbackRequest.builder()
                .extRefId((dto.getExtRefId() != null) ? dto.getExtRefId() : "0-0")
                .callAt(callAt)
                .target(uri)
                .namespace(dto.getNamespace())
                .data(dto.getData())
                .source(dto.getSource())
                .build();
    }

    private SavedCallbackRequestDto toDto(CallbackRequest request) throws URISyntaxException {

        return SavedCallbackRequestDto.builder()
                .extRefId(request.getExtRefId())
                .namespace(request.getNamespace())
                .source(request.getSource())
                .target(request.getTarget().toString())
                .callAt(request.getCallAt())
                .calledAt(request.getCalledAt())
                .data(request.getData())
                .status(request.getStatus())
                .statusReason(request.getStatusReason())
                .build();
    }
}
