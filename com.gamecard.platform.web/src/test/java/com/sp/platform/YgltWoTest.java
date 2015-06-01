package com.sp.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.util.Encrypt;
import com.sp.platform.util.LogEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanglei on 15/5/7.
 */
public class YgltWoTest {
    @Test
    public void testGetCode() throws Exception {
        HttpClient client = new DefaultHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("cpid", "100018"));
        formparams.add(new BasicNameValuePair("serviceid", "20000115"));
        formparams.add(new BasicNameValuePair("mobile", "15652651321"));
        formparams.add(new BasicNameValuePair("operator", "2"));
        DateTime dateTime = new DateTime();
        String time = dateTime.toString("yyyyMMddHHmmss");
        formparams.add(new BasicNameValuePair("datetime", time));
        String str = "100018" + "20000115" + "13221466560" + "2" + time + "43c802069b46c70504f631306a2b9e5b";
        formparams.add(new BasicNameValuePair("sign", md532(str)));
        System.out.println(str);
        System.out.println(md532(str));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams);

        System.out.println(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("http://58.67.196.166/rest/sendsmscode");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        String body = IOUtils.toString(httpResponse.getEntity().getContent());
        System.out.println("1<" + body + ">2");
        JSONObject object = JSON.parseObject(body.replace("\uFEFF\uFEFF", ""));
        System.out.println(object.get("code"));
        System.out.println(object.get("message"));
    }

    @Test
    public void sendCode() throws Exception{
        HttpClient client = new DefaultHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("cpid", "100018"));
        formparams.add(new BasicNameValuePair("serviceid", "20000105"));
        formparams.add(new BasicNameValuePair("orderid", "pc20150526000645675bug6041"));
        formparams.add(new BasicNameValuePair("mobile", "15652651321"));
        formparams.add(new BasicNameValuePair("operator", "2"));
        DateTime dateTime = new DateTime();
        String time = dateTime.toString("yyyyMMddHHmmss");
        formparams.add(new BasicNameValuePair("datetime", time));
        String str = "pc20150526000645675bug6041" + "100018" + "20000105" + "15652651321" + "2" + time + "43c802069b46c70504f631306a2b9e5b";
        System.out.println(str);
        formparams.add(new BasicNameValuePair("sign", Encrypt.md532(str)));
        formparams.add(new BasicNameValuePair("smscode", "123456"));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
        System.out.println(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("http://58.67.196.166/rest/smscharge");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        String body = IOUtils.toString(httpResponse.getEntity().getContent());
        System.out.println("提交验证码 " + body );
        JSONObject object = JSON.parseObject(body.replace("\uFEFF\uFEFF", ""));
    }

    private static String md532(String sourceStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return null;
    }

    private static String md516(String sourceStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return null;
    }

    @Test
    public void testJson() {
        String body = "1:\uFEFF\uFEFF{\"code\":\"0000\",\"message\":\"\"}";
        String body2 = "1<\uFEFF\uFEFF{\"code\":\"0000\",\"message\":\"\"}>2";
        String body3 = "11<{\"code\":\"0000\",\"message\":\"\"}>22";
        body2 = body2.replace("\uFEFF\uFEFF", "");
        System.out.println("1"+body2+"2");
    }
}
