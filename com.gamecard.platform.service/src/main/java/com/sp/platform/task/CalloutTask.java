package com.sp.platform.task;

import com.sp.platform.entity.UserCardLog;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * User: yangl
 * Date: 13-6-8 下午11:45
 */
@Service
@Scope("prototype")
public class CalloutTask implements Callable<String> {
    private UserCardLog userCardLog;

    @Autowired
    private UserCardLogSerivce userCardLogSerivce;

    @Override
    public String call() {
        long start = System.currentTimeMillis();

        try {
            String url = userCardLog.getCallouturl();

            if (StringUtils.isNotBlank(url)) {
                StringBuilder sendBody = new StringBuilder();
                sendBody.append("Phone=").append(userCardLog.getMobile());
                sendBody.append("&UID=").append(userCardLog.getId());

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "?" + sendBody.toString());

                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String returnBody = EntityUtils.toString(entity).trim();
                LogEnum.DEFAULT.info("外呼同步一条话单{}，返回[{}]", userCardLog, returnBody);
                if ("ok".equals(returnBody)) {
                    userCardLog.setFlag(5);
                    userCardLog.setSendnum(0);
                    userCardLogSerivce.save(userCardLog);
                    return returnFunc(start);
                }
            } else {
                LogEnum.DEFAULT.warn("话单未配置外呼地址{}", userCardLog);
            }
        } catch (IOException e) {
            LogEnum.DEFAULT.error("外呼同步出现异常{}", userCardLog, e);
        }
        LogEnum.DEFAULT.error("外呼同步失败，计数加1,{}", userCardLog);
        userCardLog.setSendnum(userCardLog.getSendnum() + 1);

        return returnFunc(start);
    }

    private String returnFunc(long start) {
        return userCardLog.getId() + "-" + userCardLog.getMobile() + "-" + userCardLog.getChannelid() + "同步耗时：" + (System.currentTimeMillis() - start);
    }

    public UserCardLog getUserCardLog() {
        return userCardLog;
    }

    public void setUserCardLog(UserCardLog userCardLog) {
        this.userCardLog = userCardLog;
    }
}
