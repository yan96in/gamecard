package com.sp.platform.task;

import com.sp.platform.cache.CardCache;
import com.sp.platform.cache.ChannelCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.entity.*;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: yangl
 * Date: 13-6-8 下午11:45
 */
@Service
@Scope("prototype")
public class UserCardTask implements Callable<String> {
    private List<SmsBillTemp> data;

    @Autowired
    private BillTempService billTempService;
    @Autowired
    private UserCardLogSerivce userCardLogSerivce;
    int cardCount = 0;
    int channelid = 0;
    String mobile = "";

    @Override
    public String call() {
        long start = System.currentTimeMillis();
        try {
            Iterator<SmsBillTemp> iterator = data.iterator();
            while (iterator.hasNext()) {
                int currentCount = 1;
                List<SmsBillTemp> list = new ArrayList<SmsBillTemp>();
                SmsBillTemp billTemp = iterator.next();
                mobile = billTemp.getMobile();
                channelid = billTemp.getChannelid();
                Paychannel paychannel = ChannelCache.get(billTemp.getChannelid());
                int count = paychannel.getFeecount();
                if (currentCount == count) {
                    list.add(billTemp);
                } else {
                    list.add(billTemp);
                    for (int i = 1; i < count; i++) {
                        if (iterator.hasNext()) {
                            billTemp = iterator.next();
                            list.add(billTemp);
                        } else {
                            return returnFunc(start, "计算结束,有剩余短信， mobile:" + mobile + ", channelid:" + channelid + ", 共计获得 " + cardCount + "张点卡");
                        }
                    }
                }
                cardCount++;
                StringBuilder smsids = new StringBuilder();
                for (SmsBillTemp temp : list) {
                    temp.setFlag(4);
                    billTempService.save(temp);
                    smsids.append(temp.getId()).append(",");
                }
                Date now = new Date();
                UserCardLog userCardLog = new UserCardLog(billTemp.getMobile(), String.valueOf(billTemp.getChannelid()), HaoduanCache.getProvince(billTemp.getMobile()),
                        HaoduanCache.getCity(billTemp.getMobile()), smsids.substring(0, smsids.length() - 1),
                        4, paychannel.getCallouturl(), paychannel.getSendcardurl(), now, now, paychannel.getCardId(), paychannel.getPriceId());
                userCardLogSerivce.save(userCardLog);
            }
        } catch (Exception e) {
            for(SmsBillTemp temp : data){
                temp.setSendnum(temp.getSendnum() + 1);
                billTempService.save(temp);
            }
            LogEnum.DEFAULT.error("点卡计算出现异常{}", data, e);
        }

        return returnFunc(start, "点卡计算结束， mobile:" + mobile + ", channelid:" + channelid + ", 共计获得 " + cardCount + "张点卡");
    }

    private String returnFunc(long start, String log) {
        return log + " 计算点卡耗时：" + (System.currentTimeMillis() - start);
    }

    public void setData(List<SmsBillTemp> data) {
        this.data = data;
    }
}
