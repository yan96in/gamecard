package com.sp.platform;

import com.sp.platform.util.Encrypt;
import com.yangl.common.Struts2Utils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

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
}
