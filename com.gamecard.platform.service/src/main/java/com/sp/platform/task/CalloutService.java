package com.sp.platform.task;

import com.sp.platform.entity.UserCardLog;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: yangl
 * Date: 13-6-8 下午11:19
 */
@Service
public class CalloutService {

    @Autowired
    private UserCardLogSerivce userCardLogSerivce;

    ThreadPoolExecutor threadPool2 = new ThreadPoolExecutor(10, 20, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public void callout() {
        List<UserCardLog> list = userCardLogSerivce.getCalloutData();
        if (list != null && list.size() > 0) {
            int i = 0;
            List<CalloutTask> temp = new ArrayList<CalloutTask>();
            for (UserCardLog userCardLog : list) {
                i++;
                CalloutTask calloutTask = (CalloutTask) AppContextHolder.getContext().getBean("calloutTask");
                calloutTask.setUserCardLog(userCardLog);
                temp.add(calloutTask);

                if (i >= 10) {
                    syncForUrl(temp);
                    temp.clear();
                    i = 0;
                }
            }
            if (temp.size() > 0) {
                syncForUrl(temp);
            }
        }
    }

    private void syncForUrl(List<CalloutTask> temp) {
        try {
            List<Future<String>> futures = threadPool2.invokeAll(temp);
            for (Future<String> future : futures) {
                String str = future.get();
                LogEnum.DEFAULT.info(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
