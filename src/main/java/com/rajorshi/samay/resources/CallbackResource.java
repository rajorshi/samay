package com.rajorshi.samay.resources;

import com.rajorshi.samay.common.ValidationError;
import com.rajorshi.samay.model.dao.CallbackSearchFilter;
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
import java.util.Collections;
import java.util.Date;


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

    @PostMapping
    @Transactional
    public ResponseEntity registerCallback(@RequestBody NewCallbackRequestDto dto)
    {
        return callbackService.registerCallback(dto);
    }

    @GetMapping
    public ResponseEntity getCallbacks(
            @QueryParam("ext_id") String extId
            , @QueryParam("ns") String namespace
            , @QueryParam("src") String src
            , @QueryParam("status") String status
            , @QueryParam("before") Date before
            , @QueryParam("after") Date after
            )
    {
        CallbackSearchFilter.CallbackSearchFilterBuilder builder = CallbackSearchFilter.builder()
                .after(after)
                .before(before)
                .source(src)
                .namespace(namespace)
                .extRefId(extId);

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
