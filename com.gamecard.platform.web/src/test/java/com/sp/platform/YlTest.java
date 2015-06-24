package com.sp.platform;

import com.sp.platform.util.Encrypt;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 翼龙移动PC
 * Created by yanglei on 15/6/21.
 */
public class YlTest {
    @Test
    public void verifyCodeTest() throws IOException {
        HttpClient client = new DefaultHttpClient();
        String url = "http://182.92.166.247:8013/pc_skhx/pc.php?";
        url = url + "method=request&tel=18705269754&cid=1085&code=01";
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("accept", "application/json;charset=UTF-8");

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httpGet);
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        System.out.println(httpResponse.getStatusLine().getStatusCode() + " : " + body);
    }

    @Test
    public void payTest() throws IOException {
        HttpClient client = new DefaultHttpClient();
        String url = "http://182.92.166.247:8013/pc_skhx/pc.php?";
        url = url + "method=confirm&orderid=0700000f2514a94600438e26&verifycode=333375";
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("accept", "application/json;charset=UTF-8");

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httpGet);
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        System.out.println(httpResponse.getStatusLine().getStatusCode() + " : " + body);
    }

    @Test
    public void test() throws UnsupportedEncodingException {
        String body = "\u5145\u503c\u8bf7\u6c42\u8fc7\u5feb";
        System.out.println(Encrypt.ascii2native(body));
    }
}
