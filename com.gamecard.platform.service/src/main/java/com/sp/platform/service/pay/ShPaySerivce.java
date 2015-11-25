package com.sp.platform.service.pay;

import com.sp.platform.entity.PcCardLog;
import com.sp.platform.util.Encrypt;
import com.sp.platform.util.IdUtils;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yanglei on 15/9/29.
 */
@Service
public class ShPaySerivce implements PayService {
    private static final String agentId = "2000106";
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public boolean checkAccount(String account) {
        return true;
    }

    @Override
    public String pay(PcCardLog pcCardLog) {
        try {
            String orderid = pcCardLog.getCardno();
            Integer trade_no = pcCardLog.getId();
            String tag = "1";

            String temp = "orderid=" + orderid +
                    "&tag=" + tag +
                    "&trade_no=" + trade_no;

            String validate = Encrypt.md532(temp + "sh452xLE7894").toLowerCase();

            temp = temp +"&validate=" + validate;

            String url = "http://chong.changyou.com/phone/genericAddPointzh.do?" + temp;

            HttpGet get = new HttpGet(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(get);
            LogEnum.DEFAULT.info(pcCardLog.getMobile() + " 搜狐充值 帐号充值: " + url);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "GBK");
            LogEnum.DEFAULT.info((pcCardLog.getMobile() + " " +httpResponse.getStatusLine().getStatusCode() + " : " + body));
            return body;
        } catch (Exception e) {
            LogEnum.DEFAULT.error(pcCardLog.getMobile() + "帐号充值失败" + e.toString());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(IdUtils.idGenerator("sh"));
    }
}
