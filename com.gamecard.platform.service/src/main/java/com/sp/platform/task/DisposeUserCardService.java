package com.sp.platform.task;

import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.yangl.common.StringUtils;
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
public class DisposeUserCardService {

    @Autowired
    private BillTempService billTempService;
    @Autowired
    private PropertyUtils propertyUtils;

    ThreadPoolExecutor threadPool2 = new ThreadPoolExecutor(10, 20, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public void process() {
        List<SmsBillTemp> list = billTempService.getNeedProcessData();
        if (list != null && list.size() > 0) {
            int i = 0;
            List<UserCardTask> temp = new ArrayList<UserCardTask>();
            String mobile = "";
            int channelid = 0;
            List<SmsBillTemp> smsBillTemps = new ArrayList<SmsBillTemp>();
            for (SmsBillTemp billTemp : list) {
                if (org.apache.commons.lang.StringUtils.indexOf(propertyUtils.getProperty("lthj.channel.id"), String.valueOf(billTemp.getChannelid())) >= 0) {
                    LogEnum.DEFAULT.info("不处理联通华建的数据" + billTemp.getId());
                    continue; //不处理联通华建的数据
                }

                if (!billTemp.getMobile().equals(mobile) && StringUtils.isNotBlank(mobile)) {
                    i++;
                    UserCardTask userCardTask = (UserCardTask) AppContextHolder.getContext().getBean("userCardTask");
                    userCardTask.setData(smsBillTemps);
                    temp.add(userCardTask);
                    smsBillTemps = new ArrayList<SmsBillTemp>();
                } else {
                    if (billTemp.getChannelid() != channelid && channelid > 0) {
                        i++;
                        UserCardTask userCardTask = (UserCardTask) AppContextHolder.getContext().getBean("userCardTask");
                        userCardTask.setData(smsBillTemps);
                        temp.add(userCardTask);
                        smsBillTemps = new ArrayList<SmsBillTemp>();
                    }
                }
                mobile = billTemp.getMobile();
                channelid = billTemp.getChannelid();
                smsBillTemps.add(billTemp);

                if (i >= 10) {
                    syncForUrl(temp);
                    temp.clear();
                    i = 0;
                }
            }

            UserCardTask userCardTask = (UserCardTask) AppContextHolder.getContext().getBean("userCardTask");
            userCardTask.setData(smsBillTemps);
            temp.add(userCardTask);
            syncForUrl(temp);
        }
    }

    private void syncForUrl(List<UserCardTask> temp) {
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
