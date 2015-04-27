package com.sp.platform;

import com.sp.platform.util.LogEnum;
import com.sp.platform.web.constants.LthjService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

/**
 * Created by yanglei on 15/4/25.
 */
public class LthjTest {
    @Test
    public void testCommitOrder(){
        try {
            HttpResponse response;
            HttpClient client = new DefaultHttpClient();
            String url = "http://220.181.87.55/TeleNotificationServers/servlet/CancelWebOrder?mobile=18911484443";
            HttpGet get = new HttpGet(url);
            client.execute(get);
            get.reset();

            LthjService lthjService = com.sp.platform.web.constants.Constants.getLthjService("1162");
            url = "http://sms.uupay.com.cn/smsservice/order/telecom.jsp?" +
                    "mobile=18911484443&serviceid=" + lthjService.getServiceid();
            get = new HttpGet(url);
            response = client.execute(get);
            LogEnum.DEFAULT.info("联通华建短信提交订单: " + url);
            LogEnum.DEFAULT.info(String.valueOf(response.getStatusLine().getStatusCode()));
            String body = IOUtils.toString(response.getEntity().getContent(), "GBK");
            LogEnum.DEFAULT.info(String.valueOf(body.indexOf("1162") >= 0));
        } catch (Exception e) {
            LogEnum.DEFAULT.error("联通华建短信提交订单出错 phone: 18911484443 - error: " + e);
        }
    }
}
