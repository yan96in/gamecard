package com.sp.platform.gateway.service.sp;

import com.alibaba.fastjson.JSON;
import com.sp.platform.gateway.response.wo.PaymentChargeResponse;
import com.sp.platform.gateway.response.wo.PaymentCodeResponse;
import com.sp.platform.util.LogEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WO+ 梦工厂
 * Created by yanglei on 15/7/3.
 */
@Service
public class SpServiceMgc {

    public PaymentCodeResponse getPaymentCode(Map<String, Object> map) throws Exception {
        try {
            HttpClient client = new DefaultHttpClient();
            //设置登录参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("outTradeNo", map.get("outTradeNo").toString()));
            formparams.add(new BasicNameValuePair("paymentUser", map.get("paymentUser").toString()));
            formparams.add(new BasicNameValuePair("appName", map.get("appName").toString()));
            formparams.add(new BasicNameValuePair("subject", map.get("subject").toString()));
            formparams.add(new BasicNameValuePair("appKey", map.get("appKey").toString()));
            formparams.add(new BasicNameValuePair("totalFee", map.get("totalFee").toString()));
            formparams.add(new BasicNameValuePair("payState", "0"));

            UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "utf-8");

            LogEnum.DEFAULT.info("申请WO+梦工厂小额渠道计费: " + IOUtils.toString(entity1.getContent()));
            //新建Http  post请求
            HttpPost httppost = new HttpPost("http://119.147.1.251/wocodecharge/getsmscode.tp");
            httppost.setEntity(entity1);

            //处理请求，得到响应
            HttpResponse httpResponse = client.execute(httppost);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(map.get("outTradeNo") + " 申请WO+梦工厂返回：" + String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
            return JSON.parseObject(body, PaymentCodeResponse.class);
        }catch (Exception e){
            LogEnum.DEFAULT.error("申请WO+梦工厂小额渠道计费异常: " + e.toString());
            PaymentCodeResponse response = new PaymentCodeResponse();
            response.setResultCode("500");
            response.setResultDescription(e.toString());
            return response;
        }
    }

    public PaymentChargeResponse paymentCharge(String linkid, String paymentCode) throws Exception {
        HttpClient client = new DefaultHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("outTradeNo", linkid));
        formparams.add(new BasicNameValuePair("paymentCodeSms", paymentCode));

        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "utf-8");

        LogEnum.DEFAULT.info("WO+梦工厂提交验证码计费: " + IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("http://119.147.1.251/wocodecharge/smallfee.tp");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(linkid + " WO+梦工厂提交验证码返回：" + String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
        return JSON.parseObject(body, PaymentChargeResponse.class);
    }
}
