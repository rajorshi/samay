package com.rajorshi.samay.service;

import com.rajorshi.samay.model.dao.CallbackSearchFilter;
import com.rajorshi.samay.resources.dto.NewCallbackRequestDto;
import org.springframework.http.ResponseEntity;

public interface CallbackService {

    ResponseEntity registerCallback(NewCallbackRequestDto callback);
    ResponseEntity findCallbacks(CallbackSearchFilter filter, int offset, int limit);

}
