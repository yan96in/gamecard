package com.sp.platform;

import com.sp.platform.util.Encrypt;
import com.yangl.common.Struts2Utils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/9/8.
 */
public class JCardTest {

    @Test
    public void checkAccount() throws IOException {
        String agentId = "2000106";
        String account = "13552922122";
        String temp = "agent_id=2000106&user_account=13552922122&time_stamp=20150908112900|||yy5690002015";

        String sign = Encrypt.md532(temp).toLowerCase();
        String url = "http://Service.800j.com/Personal/CheckAccount.aspx?" +
                "agent_id=" + agentId +
                "&user_account=" + account +
                "&sign=" + sign;


        HttpGet get = new HttpGet(url);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        System.out.println("检查帐号: " + url);
        String body = IOUtils.toString(response.getEntity().getContent(), "GBK");
        System.out.println(response.getStatusLine().getStatusCode() + " : " + body);

        body = "ret_code=0&ret_msg=非法来源IP:113.123.187.217&agent_id=2000106&user_account=&sign=368551047dda2ce8e6fea01872aedcdf";
        String[] t = body.split("&");
        if (t != null && t.length >= 1) {
            t = t[0].split("=");
            if (StringUtils.equals("0", t[1])) {
                System.out.println(true);
            } else {
                System.out.println(false);
            }
        }
    }

    @Test
    public void testSubmit() throws IOException {

        String agentId = "2000106";
        String account = "13552922122";
        String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
        String bill_id = timeStamp + "001";
        String temp = "agent_id=" + agentId +
                "&bill_id=" + bill_id +
                "&bill_time=" + timeStamp +
                "&user_account=" + account +
                "&charge_amt=0.1" +
                "&time_stamp=" + timeStamp;


        String sign = Encrypt.md532(temp + "|||yy5690002015").toLowerCase();

        temp = temp +
                "&phone=" + account +
                "&client_ip=" + "127.0.0.1" +
                "sign=" + sign;

        String url = "http://Service.800j.com/Personal/Submit.aspx?" + temp;

        HttpGet get = new HttpGet(url);

        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse = client.execute(get);
        System.out.println("帐号充值: " + url);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "GBK");
        System.out.println(httpResponse.getStatusLine().getStatusCode() + " : " + body);
    }

    @Test
    public void testResult(){
        String result = "ret_code=4&ret_msg=MD5验证错误&agent_id=2000106&bill_id=jc20151011135728786Kje6299&jnet_bill_no=&user_account=&purchase_amt=&real_jpoint=&ext_param=&sign=9f6d10ee16310c29ad79bff249021220";
        String[] s = result.split("&");
        Map<String, String> m = new HashMap<String, String>();
        for (String s1 : s) {
            String[] s2 = s1.split("=");
            if(s2.length >=2 ) {
                m.put(s2[0], s2[1]);
            }
        }
        System.out.println(m);
    }
}
