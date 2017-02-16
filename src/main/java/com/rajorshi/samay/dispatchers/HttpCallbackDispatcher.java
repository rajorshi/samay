package com.rajorshi.samay.dispatchers;

import com.rajorshi.samay.dispatchers.dto.CallbackDto;
import com.rajorshi.samay.model.repository.CallbackMethod;
import com.rajorshi.samay.model.repository.CallbackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service("HttpCallbackDispatcher")
class HttpCallbackDispatcher implements CallbackDispatcher {
    // Http dispatcher should not be used in production ever
    // given only for test purposes

    private static final int CONNECTION_TIMEOUT = 1000;
    private static final RestTemplate restTemplate;

    static {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        restTemplate = new RestTemplate(requestFactory);
    }

    @Override
    public void dispatch(final CallbackRequest callback) throws DispatcherException {

        URI uri;
        try {
            uri = callback.getTarget();
        } catch (URISyntaxException e) {
            throw new DispatcherException(e);
        }

        if(CallbackMethod.fromString(uri.getScheme()) != CallbackMethod.Http) {
            throw new DispatcherException("Unexpected dispatch method: " + uri.getScheme());
        }

        Date dispatchTime = Calendar.getInstance().getTime();
        CallbackDto dto = CallbackDto.builder()
                .ref(callback.getExtRefId())
                .source(callback.getSource())
                .scheduledAt(callback.getCallAt())
                .payload(callback.getData())
                .dispatchedAt(dispatchTime)
                .build();

        try {
            restTemplate.postForEntity(uri, dto, String.class);
        } catch (RestClientException ex) {
            throw new DispatcherException(ex);
        }
    }
}
