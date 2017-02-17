package com.rajorshi.samay.resources.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewCallbackRequestDto {

    @NotNull
    String extRefId;
    @NotNull
    String namespace;
    @NotNull
    String source;
    @NotNull
    String targetUrl;
    @NotNull
    Date at;
    int after;
    @NotNull
    String data;

}
