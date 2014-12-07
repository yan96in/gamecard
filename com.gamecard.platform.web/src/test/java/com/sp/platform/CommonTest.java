package com.sp.platform;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-25
 * Time: 上午7:12
 * To change this template use File | Settings | File Templates.
 */
public class CommonTest {
    @Test
    public void timeFormatTest() {
        String endtime = "2011-04-05 01:02:39";
        endtime = endtime.substring(0, 4) + endtime.substring(5, 7) + endtime.substring(8, 10) + endtime.substring(11, 13) + endtime.substring(14, 16) + endtime.substring(17, 19);
        System.out.println(endtime);
    }

    @Test
    public void testGetResult() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();


//        HttpHost proxy = new HttpHost("dev-proxy.db.rakuten.co.jp", 9506);
//        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);

        HttpGet httpGet = new HttpGet("http://pc.rsbwl.com/CBS/jfpc/zhjw_cuc.jsp?bid=100&uid=13212341234");
        HttpResponse response2 = httpClient.execute(httpGet);

        System.out.println(String.valueOf(response2.getStatusLine().getStatusCode()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
        String body = reader.readLine();
        int i = 0;
        while (StringUtils.isNotBlank(body)) {
            System.out.println(++i + ":" + body);
            body = reader.readLine();
            System.out.println(++i + ":" + body);
        }
    }

    @Test
    public void testGetSendCode() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        String sid = "";
        String scode = "";
        HttpGet httpGet = new HttpGet("http://pc.rsbwl.com/CBS/jfpc/wyorder_cuc.jsp&sid="
                + sid + "&vcode=" + scode);
        HttpResponse response2 = httpClient.execute(httpGet);

        System.out.println(String.valueOf(response2.getStatusLine().getStatusCode()));

        String body = IOUtils.toString(response2.getEntity().getContent(), "UTF-8");
        System.out.println(body);
    }
}
