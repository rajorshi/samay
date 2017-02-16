package com.rajorshi.samay.dispatchers;

import com.rajorshi.samay.model.repository.CallbackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("KafkaCallbackDispatcher")
public class KafkaCallbackDispatcher implements CallbackDispatcher {
    @Override
    public void dispatch(CallbackRequest callback) throws DispatcherException {
        throw new UnsupportedOperationException("KafkaCallbackDispatcher is not supported yet");
    }
}
