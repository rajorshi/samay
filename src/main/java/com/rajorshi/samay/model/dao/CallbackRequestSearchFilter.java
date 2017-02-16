package com.rajorshi.samay.model.dao;

import com.rajorshi.samay.model.repository.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallbackRequestSearchFilter {

    private String extRefId;
    private String namespace;
    private String source;
    private RequestStatus status;
    private Date after;
    private Date before;

}
