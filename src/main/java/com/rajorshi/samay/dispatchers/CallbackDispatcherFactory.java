package com.rajorshi.samay.dispatchers;

import com.rajorshi.samay.model.repository.CallbackMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.net.URI;

@Slf4j
@Service("CallbackDispatcherFactory")
public class CallbackDispatcherFactory {

    @Inject
    HttpCallbackDispatcher httpCallbackDispatcher;
    @Inject
    KafkaCallbackDispatcher kafkaCallbackDispatcher;

    public CallbackDispatcher getDispatcher(URI target) throws DispatcherException {

        CallbackMethod method;
        try {
            method = CallbackMethod.fromString(target.getScheme());
        } catch (IllegalArgumentException ex) {
            throw new DispatcherException(ex);
        }
        switch (method) {
            case Http:
                return httpCallbackDispatcher;
            case Kafka:
                return kafkaCallbackDispatcher;
            default:
                throw new DispatcherException("Dispatcher not available for callback method" + method);
        }
    }
}
