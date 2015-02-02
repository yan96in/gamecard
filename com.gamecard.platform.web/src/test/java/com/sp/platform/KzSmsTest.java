package com.sp.platform;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by yanglei on 15/1/14.
 */
public class KzSmsTest {
    @Test
    public void step1() throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://202.108.24.55:8081/mobileNotify.jsp?mobile=13552922122&type=1");
        HttpResponse response = client.execute(get);
        System.out.println(response.getStatusLine().getStatusCode());
        String body = IOUtils.toString(response.getEntity().getContent(), "GBK");
        System.out.println(1);
        System.out.println(body);
        System.out.println(2);
        System.out.println(StringUtils.trim(body));
        System.out.println(3);
    }
}
