package com.sp.platform.cache;

import com.sp.platform.entity.WoAppConfig;
import com.sp.platform.service.WoAppConfigService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.HttpUtils;
import com.sp.platform.util.LogEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WoAppConfigCache extends AbstractBaseTimer {
    @Autowired
    private WoAppConfigService woAppConfigService;

    /**
     * 小额计费相关缓存
     */
    private static Map<String, WoAppConfig> appKeyCache = new HashMap<String, WoAppConfig>();

    private static boolean isFirst = true;

    @Override
    public void init() {
        setName(this.getClass().getName());//小额计费
    }

    @Override
    public void update() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(-25);
        Date validTime = dateTime.toDate();
        List<WoAppConfig> list = woAppConfigService.getAll();
        appKeyCache.clear();
        for (WoAppConfig woAppConfig : list) {
            appKeyCache.put(woAppConfig.getAppKey(), woAppConfig);

            if (!isFirst) {
                if (woAppConfig.getUtime().before(validTime)) {
                    updateToken(woAppConfig);
                }
            }
        }
    }

    public void updateToken(WoAppConfig woAppConfig) {
        try {
            HttpClient httpClient = HttpUtils.getSecuredHttpClient();

            HttpGet get = new HttpGet("http://open.wo.com.cn/openapi/authenticate/v1.0?" +
                    "appKey=" + woAppConfig.getAppKey() + "&" +
                    "appSecret=" + woAppConfig.getAppSecret());

            get.addHeader("Content-Type", "application/json;charset=UTF-8");
            get.addHeader("accept", "application/json;charset=UTF-8");

            HttpResponse httpResponse = httpClient.execute(get);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info("更新Token "+String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
            woAppConfig.setAppToken(body);
            woAppConfig.setUtime(new Date());
            woAppConfigService.save(woAppConfig);
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用WO+接口异常：" + e.toString());
        }
    }

    public WoAppConfig get(String appKey) {
        return appKeyCache.get(appKey);
    }

    public String getToken(String appKey) {
        WoAppConfig config = appKeyCache.get(appKey);
        if (config == null) {
            return "";
        }
        return config.getAppToken();
    }
}