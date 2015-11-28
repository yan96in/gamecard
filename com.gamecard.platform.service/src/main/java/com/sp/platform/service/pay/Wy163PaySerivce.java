package com.sp.platform.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.constants.Constants;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.sign.EsalesRAS;
import com.sp.platform.util.IdUtils;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yanglei on 15/9/29.
 */
@Service
public class Wy163PaySerivce implements PayService {
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public boolean checkAccount(String account) {
        return true;
    }

    @Override
    public String pay(PcCardLog pcCardLog) {
        try {
            String orderId = IdUtils.idGenerator("wy");
            DateTime date = new DateTime();
            String d = date.toString("yyyyMMddHHmmss");

            String site_id = propertyUtils.getProperty("163.site_id");
            String user_id = propertyUtils.getProperty("163.user_id");
            String privateKey = propertyUtils.getProperty("163.private_key");
            String publicKey = propertyUtils.getProperty("163.public_key");
            String url = propertyUtils.getProperty("163.charge_url");

            String account = pcCardLog.getCardno();
            String order_id = orderId;
            String order_time = d;
            String urs = pcCardLog.getCardno();
            String reason = "1";
            String pts = Constants.jcardFee.get(pcCardLog.getCardId() + "" + pcCardLog.getPriceId()).intValue() + "";
            StringBuilder builder = new StringBuilder();
            builder.append("?site_id=").append(site_id);
            builder.append("&user_id=").append(user_id);
            builder.append("&order_id=").append(orderId);
            builder.append("&order_time=").append(d);
            builder.append("&urs=").append(urs);
            builder.append("&reason=").append(reason);
            builder.append("&pts=").append(pts);

            String key = site_id + user_id + order_id + order_time + urs + reason + pts;
            //私钥加密过程
            EsalesRAS ras = new EsalesRAS();

            String signature = ras.generateSHA1withRSASigature(key, privateKey);

            builder.append("&sign=").append(signature);

            HttpClient httpClient = com.sp.platform.util.HttpUtils.getSecuredHttpClient();
            LogEnum.DEFAULT.info(pcCardLog.getMobile() + " -> 帐号充值: " + url + builder.toString());
            HttpGet get = new HttpGet(url + builder.toString());
            HttpResponse httpResponse = httpClient.execute(get);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(pcCardLog.getMobile() + " -> " + String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);


            JSONObject object = JSON.parseObject(body);
            JSONArray jsonArray = object.getJSONArray("sign_fields");
            Object[] fields = jsonArray.toArray();
            String source = "";
            for (Object str : fields) {
                source = source + object.get(str).toString();
            }
            String sign_org = object.get("sign").toString();
            boolean flag = ras.verifySHA1withRSASigature(sign_org, source, publicKey);
            if (flag) {
                String status = object.getString("status").toUpperCase();
                if (StringUtils.equals("OK", status) || StringUtils.equals("DUP", status)) {
                    return "0";
                } else {
                    pcCardLog.setResultcode(status);
                    pcCardLog.setResultmsg(object.getString("reason"));
                    return "error";
                }
            }
            return "error";
        } catch (Exception e) {
            LogEnum.DEFAULT.error(pcCardLog.getMobile() + "帐号充值失败" + e.toString());
            return "error";
        }
    }
}
