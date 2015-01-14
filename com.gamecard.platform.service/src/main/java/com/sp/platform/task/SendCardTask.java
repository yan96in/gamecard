package com.sp.platform.task;

import com.sp.platform.cache.CardCache;
import com.sp.platform.cache.ChannelCache;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.service.CardPasswordService;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.util.XDEncodeHelper;
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
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * User: yangl
 * Date: 13-6-8 下午11:45
 */
@Service
@Scope("prototype")
public class SendCardTask implements Callable<String> {
    private UserCardLog userCardLog;

    @Autowired
    private UserCardLogSerivce userCardLogSerivce;
    @Autowired
    private CardPasswordService cardPasswordService;
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public String call() {
        long start = System.currentTimeMillis();

        try {
            String url = userCardLog.getSendcardurl();
            String returnOk = "ok";
            String message = "";
            CardPassword card = null;
            userCardLog.setEtime(new Date());
            if (StringUtils.isNotBlank(url)) {
                StringBuilder sendBody = new StringBuilder();
                LogEnum.DEFAULT.info(url);
                LogEnum.DEFAULT.info(propertyUtils.getProperty("hljw.qxt.url"));
                if (url.equals(propertyUtils.getProperty("hljw.qxt.url"))) {
                    Paychannel paychannel = ChannelCache.get(Integer.parseInt(userCardLog.getChannelid()));
                    if (paychannel == null) {
                        userCardLog.setSendnum(userCardLog.getSendnum() + 1);
                        userCardLogSerivce.save(userCardLog);
                        LogEnum.DEFAULT.error("下发点卡参数异常{}", userCardLog);
                        return returnFunc(start);
                    }
                    if (StringUtils.isBlank(userCardLog.getCardno())) {
                        card = cardPasswordService.getUserCard(paychannel.getCardId(), paychannel.getPriceId());
                        if (card == null) {
                            userCardLog.setSendnum(userCardLog.getSendnum() + 1);
                            userCardLogSerivce.save(userCardLog);
                            LogEnum.DEFAULT.error("下发点卡失败，无卡{}", userCardLog);
                            return returnFunc(start);
                        }
                        XDEncodeHelper xdEncodeHelper = new XDEncodeHelper(propertyUtils.getProperty("DESede.key", "tch5VEeZSAJ2VU4lUoqaYddP"));

                        userCardLog.setCardno(xdEncodeHelper.XDDecode(card.getCardno(), true));
                        userCardLog.setCardpwd(xdEncodeHelper.XDDecode(card.getPassword(), true));
                    }
                    message = MessageFormat.format(propertyUtils.getProperty("hljw.card.message"),
                            CardCache.getPrice(paychannel.getPriceId()).getDescription() + CardCache.getCard(paychannel.getCardId()).getName(),
                            userCardLog.getCardno(), userCardLog.getCardpwd());
                    sendBody.append("username=").append(propertyUtils.getProperty("hljw.qxt.username"));
                    sendBody.append("&password=").append(propertyUtils.getProperty("hljw.qxt.password"));
                    sendBody.append("&epid=").append(propertyUtils.getProperty("hljw.qxt.epid"));
                    sendBody.append("&phone=").append(userCardLog.getMobile());
                    sendBody.append("&message=").append(URLEncoder.encode(message,"gb2312"));
                    sendBody.append("&linkid=").append(userCardLog.getId());
                    returnOk = propertyUtils.getProperty("hljw.qxt.return");
                } else {
                    userCardLog.setSendnum(userCardLog.getSendnum() + 1);
                    userCardLogSerivce.save(userCardLog);
                    LogEnum.DEFAULT.error("下发点卡失败，下发地址暂未配置{}", userCardLog);
                    return returnFunc(start);
                }

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "?" + sendBody.toString());

                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String returnBody = EntityUtils.toString(entity).trim();
                LogEnum.DEFAULT.info("点卡下发{}，返回[{}]", sendBody.toString(), returnBody);
                if (returnOk.equals(returnBody)) {
                    userCardLog.setFlag(7);
                    userCardLogSerivce.save(userCardLog);
                    return returnFunc(start);
                }
            } else {
                LogEnum.DEFAULT.warn("话单未配置下发点卡地址{}", userCardLog);
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("下发点卡出现异常{}", userCardLog, e);
        }
        LogEnum.DEFAULT.error("下发点卡失败，计数加1,{}", userCardLog);
        userCardLog.setSendnum(userCardLog.getSendnum() + 1);
        userCardLogSerivce.save(userCardLog);


        return returnFunc(start);
    }

    private String returnFunc(long start) {
        return userCardLog.getId() + "-" + userCardLog.getMobile() + "-" + userCardLog.getChannelid() + "下发点卡耗时：" + (System.currentTimeMillis() - start);
    }

    public UserCardLog getUserCardLog() {
        return userCardLog;
    }

    public void setUserCardLog(UserCardLog userCardLog) {
        this.userCardLog = userCardLog;
    }
}
