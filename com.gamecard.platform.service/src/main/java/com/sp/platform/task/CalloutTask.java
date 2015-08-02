package com.sp.platform.task;

import com.sp.platform.cache.CardCache;
import com.sp.platform.cache.ChannelCache;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
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
import java.net.URLEncoder;
import java.text.MessageFormat;
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
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public String call() {
        long start = System.currentTimeMillis();

        try {
            if (StringUtils.contains(propertyUtils.getProperty("kz.sms.new.paychannel.id"), userCardLog.getChannelid())) {
                LogEnum.DEFAULT.info("空中新短信略过外呼步骤" + userCardLog);
                userCardLog.setFlag(6);
                userCardLog.setSendnum(0);
                userCardLogSerivce.save(userCardLog);
                return returnFunc(start);
            }

            if (StringUtils.contains(propertyUtils.getProperty("kz.sms.paychannel.id"), userCardLog.getChannelid())) {
                sendSmsToUser();
                return returnFunc(start);
            }

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
        userCardLogSerivce.save(userCardLog);

        return returnFunc(start);
    }

    private boolean sendSmsToUser() throws IOException {
        CardPassword card;
        String message;
        String returnOk;
        StringBuilder sendBody = new StringBuilder();
        String url = propertyUtils.getProperty("hljw.qxt.url");
        LogEnum.DEFAULT.info(url);

        Paychannel paychannel = ChannelCache.get(Integer.parseInt(userCardLog.getChannelid()));
        if (paychannel == null) {
            userCardLog.setSendnum(userCardLog.getSendnum() + 1);
            userCardLogSerivce.save(userCardLog);
            LogEnum.DEFAULT.error("空中短信购买下发提示参数异常{}", userCardLog);
            return true;
        }

        message = MessageFormat.format(propertyUtils.getProperty("kz.sms.qxt.user.mo"),
                CardCache.getPrice(paychannel.getPriceId()).getDescription() + CardCache.getCard(paychannel.getCardId()).getName());
        sendBody.append("username=").append(propertyUtils.getProperty("hljw.qxt.username"));
        sendBody.append("&password=").append(propertyUtils.getProperty("hljw.qxt.password"));
        sendBody.append("&epid=").append(propertyUtils.getProperty("hljw.qxt.epid"));
        sendBody.append("&phone=").append(userCardLog.getMobile());
        sendBody.append("&message=").append(URLEncoder.encode(message, "gb2312"));
        sendBody.append("&linkid=").append(userCardLog.getId());
        returnOk = propertyUtils.getProperty("hljw.qxt.return");

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url + "?" + sendBody.toString());

        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String returnBody = EntityUtils.toString(entity).trim();
        LogEnum.DEFAULT.info("空中短信购买提示下发{}，返回[{}]", sendBody.toString(), returnBody);
        if (returnOk.equals(returnBody)) {
            userCardLog.setFlag(5);
            userCardLog.setSendnum(0);
            userCardLogSerivce.save(userCardLog);
            return true;
        }
        return false;
    }

    private String returnFunc(long start) {
        return userCardLog.getId() + "-" + userCardLog.getMobile() + "-" + userCardLog.getChannelid() + "外呼同步耗时：" + (System.currentTimeMillis() - start);
    }

    public UserCardLog getUserCardLog() {
        return userCardLog;
    }

    public void setUserCardLog(UserCardLog userCardLog) {
        this.userCardLog = userCardLog;
    }
}
