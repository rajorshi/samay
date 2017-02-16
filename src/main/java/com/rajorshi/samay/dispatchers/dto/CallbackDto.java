package com.rajorshi.samay.dispatchers.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CallbackDto {

    @NotNull
    String ref;
    @NotNull
    String source;
    @NotNull
    Date scheduledAt;
    @NotNull
    Date dispatchedAt;
    @NotNull
    String payload;

}
