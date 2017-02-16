package com.rajorshi.samay.jobs;

import com.rajorshi.samay.configuration.TimeServiceConfiguration;
import com.rajorshi.samay.model.dao.CallbackRequestDao;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service("PurgeOldCallbacksJob")
public class PurgeOldCallbacksJob implements Job {

    private static final int BATCH_SIZE = 100;
    private static final int MAX_REQUEST_PERSISTENCE_DAYS = 14;

    @Inject
    private CallbackRequestDao callbackRequestDao;
    @Inject
    private TimeServiceConfiguration timeServiceConfiguration;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws org.quartz.JobExecutionException {

        int days = context.getMergedJobDataMap().getInt(JobDataMapKeys.DAYS);
        callbackRequestDao.deleteCallbacksByDate(purgeTill(days), BATCH_SIZE);
    }

    private Date purgeTill(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, days * -24);
        return calendar.getTime();
    }

}
