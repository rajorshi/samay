package com.rajorshi.samay.jobs;

import com.rajorshi.samay.dispatchers.CallbackDispatcher;
import com.rajorshi.samay.dispatchers.CallbackDispatcherFactory;
import com.rajorshi.samay.dispatchers.DispatcherException;
import com.rajorshi.samay.model.dao.TimedCallbackRequestDao;
import com.rajorshi.samay.model.repository.CallbackRequest;
import com.rajorshi.samay.model.repository.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("DispatchCallbacksJob")
@PersistJobDataAfterExecution
public class DispatchCallbacksJob implements Job {

    private static final int BATCH_SIZE = 10;
    private static final int CALLBACK_EXPIRY_HOURS = 6;

    @Inject
    private TimedCallbackRequestDao timedCallbackRequestDao;
    @Inject
    private CallbackDispatcherFactory dispatcherFactory;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String ns = context.getMergedJobDataMap().get(JobDataMapKeys.NAMESPACE).toString();
        List<CallbackRequest> callbacks = timedCallbackRequestDao.getPendingCallbacks(
                ns
                , Calendar.getInstance().getTime()
                , BATCH_SIZE
        );

        for (CallbackRequest callback : callbacks) {
            if (callbackExpired(callback)) {
                callback.status(RequestStatus.Expired,
                        "Marked expired on: " + Calendar.getInstance().getTime().toString());
                log.info("Callback expired: {}", callback.toString());
            } else {
                try {
                    CallbackDispatcher dispatcher = dispatcherFactory.getDispatcher(callback.getTarget());
                    dispatcher.dispatch(callback);
                    callback.calledAt(Calendar.getInstance().getTime());
                    callback.status(RequestStatus.Dispatched, "ok");
                    log.info("Callback dispatched: {}", callback.toString());
                } catch (URISyntaxException | DispatcherException e) {
                    callback.status(RequestStatus.DispatchFailed, e.getMessage());
                    log.error(e.getMessage());
                }
            }
        }
        timedCallbackRequestDao.save(callbacks);
    }

    private boolean callbackExpired(CallbackRequest callback) {

        Date now = Calendar.getInstance().getTime();
        Date callAt = callback.getCallAt();
        if (callAt.before(now)) {
            long diffInHours = TimeUnit.MILLISECONDS.toHours(now.getTime() - callAt.getTime());
            return diffInHours > CALLBACK_EXPIRY_HOURS;
        }
        return false;
    }

}
