package com.rajorshi.samay.dispatchers;

import com.rajorshi.samay.model.repository.CallbackRequest;

public interface CallbackDispatcher {

    void dispatch(final CallbackRequest callback) throws DispatcherException;

}
