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

    String namespace;
    String extRefId;
    String source;
    String target;
    String data;
    Date callAt;
    Date calledAt;
    //    private String timeZone;
    RequestStatus status;
    String statusReason;

}
