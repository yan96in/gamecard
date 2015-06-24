package com.sp.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.Encrypt;
import com.sp.platform.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/5/19.
 */
public class WoTest {

    @Test
    public void getToken2() {
        try {
            HttpClient httpClient = HttpUtils.getSecuredHttpClient();

            HttpGet get = new HttpGet("http://open.wo.com.cn/openapi/authenticate/v1.0?" +
                    "appKey=d8e115b82c4a30eea113cd1c296853d8758553a9&" +
                    "appSecret=8a8d144c1271987c05b6aa094941ca2ed8da15b0");

            get.addHeader("Content-Type", "application/json;charset=UTF-8");
            get.addHeader("accept", "application/json;charset=UTF-8");

            HttpResponse httpResponse = httpClient.execute(get);
            LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(body); //e4771cc5579f85b539c3467a6ebc0f56c276a0f8  忍者斗地主
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用WO+接口异常：" + e.toString());
        }
    }

    @Test //计费2.0 请求验证码
    public void sdk2SendPaymentCodeV2Test() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("outTradeNo", "20150519184337313VOi4721");
            map.put("paymentUser", "15652651321"); //13292640301  15564678648 15652651321  13141151451
            map.put("paymentAcount", "001");
            map.put("paymentType", 0);
            map.put("subject", "金币");
            map.put("t" +
                    "talFee", 0.1);
            String jsonBody = JSON.toJSONString(map);
            LogEnum.DEFAULT.info("请求计费2.0: " + jsonBody);
            HttpClient httpClient = HttpUtils.getSecuredHttpClient();

            HttpPost post = new HttpPost("https://open.wo.com.cn/openapi/rpc/paymentcodesms/v2.0");
            StringEntity se = new StringEntity(jsonBody, "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.addHeader("Authorization", "appKey=\"d8e115b82c4a30eea113cd1c296853d8758553a9\",token=\"e4771cc5579f85b539c3467a6ebc0f56c276a0f8\"");
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("accept", "application/json;charset=UTF-8");
            post.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(post);
            LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(body);
            JSONObject result = JSON.parseObject(body);
            System.out.println(result.get("resultCode"));
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用WO+接口异常：" + e.toString());
        }
    }

    @Test //计费2.0 提交验证码
    public void sdk2ChannelPaymentChargeV2Test() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("paymentUser", "15652651321"); //13292640301  15564678648 15562092589  13141151451
            map.put("paymentAcount", "001");
            map.put("outTradeNo", "20150519184337313VOi4728");
            map.put("subject", "金币");
            map.put("totalFee", 0.1);
            DateTime dateTime = new DateTime();
            map.put("timeStamp", dateTime.toString("yyyyMMddHHmmss"));
            map.put("paymentcodesms", 289538);

            String signature = Encrypt.encryptHmacSha1(map, "d8e115b82c4a30eea113cd1c296853d8758553a9&8a8d144c1271987c05b6aa094941ca2ed8da15b0");

            map.put("signType", "HMAC-SHA1");
            map.put("signature", signature);

            String jsonBody = JSON.toJSONString(map);
            LogEnum.DEFAULT.info("提交WO+小额渠道计费验证码: " + jsonBody);
            HttpClient httpClient = HttpUtils.getSecuredHttpClient();

            HttpPost post = new HttpPost("https://open.wo.com.cn/openapi/rpc/apppayment/v2.0");
            StringEntity se = new StringEntity(jsonBody, "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.addHeader("Authorization", "appKey=\"d8e115b82c4a30eea113cd1c296853d8758553a9\",token=\"e4771cc5579f85b539c3467a6ebc0f56c276a0f8\"");
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("accept", "application/json;charset=UTF-8");
            post.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(post);
            LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(body);
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用WO+接口异常：" + e.toString());
        }
    }

}
