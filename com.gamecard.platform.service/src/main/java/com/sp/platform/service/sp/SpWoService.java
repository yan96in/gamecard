package com.sp.platform.service.sp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.cache.WoAppConfigCache;
import com.sp.platform.constants.Constants;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.util.*;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.LtPcResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/6/22.
 */
@Service
public class SpWoService {
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private WoAppConfigCache woAppConfigCache;

    public LtPcResult sendCode(String phone, BigDecimal fee) throws IOException {
        LtPcResult pcResult = new LtPcResult();
        ChannelVo chanels = new ChannelVo();
        String appKey = propertyUtils.getProperty("wo.appKey");
        String appSecret = propertyUtils.getProperty("wo.appSecret");
        String appToken = woAppConfigCache.getToken(appKey);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String outTradeNo = IdUtils.idGenerator("gw");
            map.put("outTradeNo", outTradeNo);
            map.put("paymentUser", phone); //13292640301  15564678648 15652651321  13141151451
            map.put("paymentAcount", "001");
            map.put("paymentType", 0);
            map.put("subject", "金币");
            map.put("totalFee", fee);
            String jsonBody = JSON.toJSONString(map);
            LogEnum.DEFAULT.info("请求计费2.0: " + jsonBody);
            HttpClient httpClient = HttpUtils.getSecuredHttpClient();

            HttpPost post = new HttpPost("https://open.wo.com.cn/openapi/rpc/paymentcodesms/v2.0");
            StringEntity se = new StringEntity(jsonBody, "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.addHeader("Authorization", "appKey=\"" + appKey + "\",token=\"" + appToken + "\"");
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("accept", "application/json;charset=UTF-8");
            post.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(post);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(phone + " 【WO+】联通申请指令返回1:" + body);

            JSONObject result = JSON.parseObject(body);
            if (StringUtils.equals("0", result.getString("resultCode"))) {
                chanels.setPcflag(true);
                chanels.setSid(outTradeNo);
                pcResult.setChanels(chanels);
                pcResult.setFlag(true);
                pcResult.setSid(outTradeNo);
                return pcResult;
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用WO+接口异常：" + e.toString());
        }
        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        pcResult.setFlag(false);
        return pcResult;
    }

    public String commitPaymentCode(String phone, String code, String sid, BigDecimal fee) throws IOException {

        String appKey = propertyUtils.getProperty("wo.appKey");
        String appSecret = propertyUtils.getProperty("wo.appSecret");
        String appToken = woAppConfigCache.getToken(appKey);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paymentUser", phone); //13292640301  15564678648 15562092589  13141151451
        map.put("paymentAcount", "001");
        map.put("outTradeNo", sid);
        map.put("subject", "金币");
        map.put("totalFee", fee);
        DateTime dateTime = new DateTime();
        map.put("timeStamp", dateTime.toString("yyyyMMddHHmmss"));
        map.put("paymentcodesms", Integer.parseInt(code));

        String key = appKey + "&" + appSecret;
        String signature = Encrypt.encryptHmacSha1(map, key);

        map.put("signType", "HMAC-SHA1");
        map.put("signature", signature);

        String jsonBody = JSON.toJSONString(map);
        LogEnum.DEFAULT.info("提交WO+计费2.0验证码: " + jsonBody);
        HttpClient httpClient = HttpUtils.getSecuredHttpClient();

        HttpPost post = new HttpPost("https://open.wo.com.cn/openapi/rpc/apppayment/v2.0");
        StringEntity se = new StringEntity(jsonBody, "UTF-8");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
        post.addHeader("Authorization", "appKey=\"" + appKey + "\",token=\"" + appToken + "\"");
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.addHeader("accept", "application/json;charset=UTF-8");
        post.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(post);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
        return body;
    }

}
