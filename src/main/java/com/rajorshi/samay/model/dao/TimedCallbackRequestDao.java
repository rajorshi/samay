package com.rajorshi.samay.model.dao;

import com.rajorshi.samay.model.repository.CallbackRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface TimedCallbackRequestDao extends Serializable {

    CallbackRequest createNewTimedCallback(CallbackRequest callback);

    CallbackRequest save(CallbackRequest callback);

    List<CallbackRequest> save(List<CallbackRequest> callbacks);

    List<CallbackRequest> getPendingCallbacks(String namespace, Date date, long limit);

    List<CallbackRequest> findCallbacks(CallbackSearchFilter filter, int offset, int limit);

    int deleteCallbacksByDate(Date date, long limit);
}
