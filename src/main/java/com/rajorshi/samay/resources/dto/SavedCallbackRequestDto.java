package com.rajorshi.samay.resources.dto;

import com.rajorshi.samay.model.repository.RequestStatus;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SavedCallbackRequestDto {

    private String namespace;
    private String extRefId;
    private String source;
    private String target;
    private String data;
    private Date callAt;
    private Date calledAt;
    private RequestStatus status;
    private String statusReason;

}
