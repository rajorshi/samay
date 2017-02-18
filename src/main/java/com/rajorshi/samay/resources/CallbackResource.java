package com.rajorshi.samay.resources;

import com.rajorshi.samay.common.ValidationError;
import com.rajorshi.samay.model.dao.CallbackRequestSearchFilter;
import com.rajorshi.samay.model.repository.RequestStatus;
import com.rajorshi.samay.resources.dto.NewCallbackRequestDto;
import com.rajorshi.samay.service.CallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;


@Slf4j
@RestController
@RequestMapping(value = "/callbacks")
public class CallbackResource {

    @Inject
    private CallbackService callbackService;

    @Inject
    public CallbackResource(CallbackService callbackService)
    {
        this.callbackService = callbackService;
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE
        , produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Transactional
    public ResponseEntity registerCallback(@RequestBody NewCallbackRequestDto dto)
    {
        return callbackService.registerCallback(dto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCallbacks(
        @QueryParam("ext_id") String extId
        , @QueryParam("ns") String namespace
        , @QueryParam("src") String src
        , @QueryParam("status") String status
        , @QueryParam("before") String beforeStr
        , @QueryParam("after") String afterStr
        )
    {
        CallbackRequestSearchFilter.CallbackRequestSearchFilterBuilder builder = CallbackRequestSearchFilter.builder()
                .source(src)
                .namespace(namespace)
                .extRefId(extId);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH-mm", Locale.ENGLISH);
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy HH-mm", Locale.ENGLISH);
        if (beforeStr != null && !beforeStr.isEmpty()) {
            try {
                builder.before(format.parse(beforeStr)); // do not use
            } catch (ParseException e) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                Collections.singletonList(
                                        new ValidationError(0, Collections.singletonList(e.getMessage()))
                                )
                        );
            }
        }
        if (afterStr != null && !afterStr.isEmpty()) {
            try {
                builder.after(format.parse(afterStr)); // do not use
            } catch (ParseException e) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                Collections.singletonList(
                                        new ValidationError(0, Collections.singletonList(e.getMessage()))
                                )
                        );
            }
        }
        if (status != null) {
            try {
                builder.status(RequestStatus.fromString(status));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                Collections.singletonList(
                                        new ValidationError(0, Collections.singletonList(e.getMessage()))
                                )
                        );
            }
        }
        return callbackService.findCallbacks(builder.build(), 0, 100);
    }

}
