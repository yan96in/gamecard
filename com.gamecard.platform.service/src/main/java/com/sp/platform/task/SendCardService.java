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
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-7
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SendCardService {

    @Autowired
    private UserCardLogSerivce userCardLogSerivce;


    ThreadPoolExecutor threadPool2 = new ThreadPoolExecutor(10, 20, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public void sendCard(){
        List<UserCardLog> list = userCardLogSerivce.getSendCardData();
        if (list != null && list.size() > 0) {
            int i = 0;
            List<SendCardTask> temp = new ArrayList<SendCardTask>();
            for (UserCardLog userCardLog : list) {
                i++;
                SendCardTask sendCardTask = (SendCardTask) AppContextHolder.getContext().getBean("sendCardTask");
                sendCardTask.setUserCardLog(userCardLog);
                temp.add(sendCardTask);

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

    private void syncForUrl(List<SendCardTask> temp) {
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
