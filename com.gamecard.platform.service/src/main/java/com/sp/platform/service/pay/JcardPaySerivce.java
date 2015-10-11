package com.sp.platform.service.pay;

import com.sp.platform.entity.PcCardLog;
import com.sp.platform.util.Encrypt;
import com.sp.platform.util.IdUtils;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
public class JcardPaySerivce implements PayService {
    private static final String agentId = "2000106";
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public boolean checkAccount(String account) {
        try {
            DateTime dateTime = new DateTime();
            String timeStamp = dateTime.toString("yyyyMMddHHmmss");
            String temp = "agent_id=" + agentId + "&user_account=" + account + "&time_stamp="
                    + timeStamp + "|||yy5690002015";

            String sign = Encrypt.md532(temp).toLowerCase();
            String url = propertyUtils.getProperty("jCard.checkAccount.url") +
                    "?agent_id=" + agentId +
                    "&user_account=" + account +
                    "&time_stamp=" + timeStamp +
                    "&sign=" + sign;

            HttpGet get = new HttpGet(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            LogEnum.DEFAULT.info("骏网直充检查帐号: " + url);
            String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            int code = response.getStatusLine().getStatusCode();
            LogEnum.DEFAULT.info(account + " : " + code + " : " + body);
            if (code == 200) {
                String[] t = body.split("&");
                if (t != null && t.length >= 1) {
                    t = t[0].split("=");
                    if (StringUtils.equals("0", t[1])) {
                        return true;
                    }
                }
            } else {
                LogEnum.DEFAULT.info("骏网检查帐号返回状态异常：" + code);
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
        return false;
    }

    @Override
    public String pay(PcCardLog pcCardLog) {
        try {
            String account = pcCardLog.getCardno();
            String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
            String bill_id = IdUtils.idGenerator("jc");
            String temp = "agent_id=" + agentId +
                    "&bill_id=" + bill_id +
                    "&bill_time=" + timeStamp +
                    "&user_account=" + account +
                    "&charge_amt=" + pcCardLog.getFee()/100 +
                    "&time_stamp=" + timeStamp;


            String sign = Encrypt.md532(temp + "|||yy5690002015").toLowerCase();

            temp = temp +
                    "&phone=" + account +
                    "&client_ip=" + pcCardLog.getIp() +
                    "sign=" + sign;

            String url = "http://Service.800j.com/Personal/Submit.aspx?" + temp;

            HttpGet get = new HttpGet(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(get);
            LogEnum.DEFAULT.info(pcCardLog.getMobile() + "帐号充值: " + url);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info((httpResponse.getStatusLine().getStatusCode() + " : " + body));
            return null;
        } catch (Exception e) {
            LogEnum.DEFAULT.error(pcCardLog.getMobile() + "帐号充值失败" + e.toString());
            return null;
        }
    }
}
