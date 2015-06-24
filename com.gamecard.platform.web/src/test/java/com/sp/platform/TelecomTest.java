package com.sp.platform;

import com.sp.platform.util.IdUtils;
import com.sp.platform.web.util.CryptTool;
import com.sp.platform.web.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanglei on 15/6/20.
 */
public class TelecomTest {
    @Test
    public void verifyCodeTest() throws Exception {
        HttpClient client = HttpUtils.getSecuredHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String orderid = IdUtils.idGenerator("te");
        String PHONENUM = "18911484443";
        String MERCHANTID = "023101400064000";
        formparams.add(new BasicNameValuePair("MERCHANTID", MERCHANTID));
        formparams.add(new BasicNameValuePair("ORDERSEQ", orderid));
        formparams.add(new BasicNameValuePair("ORDERREQTRANSEQ", "seq" + orderid));
        formparams.add(new BasicNameValuePair("TELEPHONE", PHONENUM));
        formparams.add(new BasicNameValuePair("FUNCTIONTYPE", "1"));
        formparams.add(new BasicNameValuePair("ORDERAMOUNT", "1"));
        StringBuilder builder = new StringBuilder();
        builder.append("MERCHANTID=").append(MERCHANTID);
        builder.append("&ORDERSEQ=").append(orderid);
        builder.append("&ORDERREQTRANSEQ=").append("seq" + orderid);
        builder.append("&TELEPHONE=").append(PHONENUM);
        builder.append("&KEY=B2861E6B4F51839C57DE429898DA90A2A66FDA16F1EF5A3C");
        System.out.println(builder.toString());
        formparams.add(new BasicNameValuePair("MAC", CryptTool.md5Digest(builder.toString())));

        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams);

        System.out.println(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("https://webpaywg.bestpay.com.cn/verifyCode.do");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        String body = IOUtils.toString(httpResponse.getEntity().getContent());
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + ":" + body);
    }

    @Test
    public void payTest() throws Exception {
        HttpClient client = HttpUtils.getSecuredHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String orderid = "te20150624202319217zAs3418";
        String PHONENUM = "18911484443";
        String MERCHANTID = "023101400064000";
        String MERCHANTPWD = "123456";
        String MERCHANTPHONE = "4000974884";
        String VERIFYCODE = "014237";
        String ORDERAMOUNT = "1";
        String USERIP = "127.0.0.1";
        DateTime dateTime = new DateTime();
        String ORDERREQTIME = dateTime.toString("yyyyMMddHHmmss");

        formparams.add(new BasicNameValuePair("MERCHANTID", MERCHANTID));
        formparams.add(new BasicNameValuePair("MERCHANTPWD", MERCHANTPWD));
        formparams.add(new BasicNameValuePair("MERCHANTPHONE", MERCHANTPHONE));
        formparams.add(new BasicNameValuePair("ORDERSEQ", orderid));
        formparams.add(new BasicNameValuePair("ORDERREQTRANSEQ", "seq2" + orderid));
        formparams.add(new BasicNameValuePair("ORDERAMOUNT", ORDERAMOUNT));
        formparams.add(new BasicNameValuePair("ORDERREQTIME", ORDERREQTIME));
        formparams.add(new BasicNameValuePair("USERACCOUNT", PHONENUM));
        formparams.add(new BasicNameValuePair("PHONENUM", PHONENUM));
        formparams.add(new BasicNameValuePair("VERIFYCODE", VERIFYCODE));
        formparams.add(new BasicNameValuePair("GOODPAYTYPE", "0"));
        formparams.add(new BasicNameValuePair("GOODSCODE", "card12"));
        formparams.add(new BasicNameValuePair("GOODSNAME", "骏网点卡"));
        formparams.add(new BasicNameValuePair("USERIP", USERIP));
        formparams.add(new BasicNameValuePair("GOODSNUM", "1"));
        formparams.add(new BasicNameValuePair("BACKMERCHANTURL", "http://139.159.0.226/test2.jsp"));

        StringBuilder builder = new StringBuilder();
        builder.append("MERCHANTID=").append(MERCHANTID);
        builder.append("&MERCHANTPWD=").append(MERCHANTPWD);
        builder.append("&ORDERSEQ=").append(orderid);
        builder.append("&ORDERREQTRANSEQ=seq2").append(orderid);
        builder.append("&ORDERREQTIME=").append(ORDERREQTIME);
        builder.append("&ORDERAMOUNT=").append(ORDERAMOUNT);
        builder.append("&USERACCOUNT=").append(PHONENUM);
        builder.append("&USERIP=").append(USERIP);
        builder.append("&PHONENUM=").append(PHONENUM);
        builder.append("&GOODPAYTYPE=").append("0");
        builder.append("&GOODSCODE=").append("card12");
        builder.append("&GOODSNUM=").append("1");
        builder.append("&KEY=B2861E6B4F51839C57DE429898DA90A2A66FDA16F1EF5A3C");

        System.out.println(builder.toString());
        formparams.add(new BasicNameValuePair("MAC", CryptTool.md5Digest(builder.toString())));

        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams);

        System.out.println(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("https://webpaywg.bestpay.com.cn/backBillPay.do");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        String body = IOUtils.toString(httpResponse.getEntity().getContent());
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + ":" + body);
    }

    @Test
    public void queryTest() throws Exception {
        HttpClient client = HttpUtils.getSecuredHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String orderid = IdUtils.idGenerator("te");
        String PHONENUM = "18911484443";
        String MERCHANTID = "023101400064000";
        formparams.add(new BasicNameValuePair("MERCHANTID", MERCHANTID));
        formparams.add(new BasicNameValuePair("ORDERSEQ", orderid));
        formparams.add(new BasicNameValuePair("ORDERREQTRANSEQ", "seq" + orderid));
        formparams.add(new BasicNameValuePair("TELEPHONE", PHONENUM));
        formparams.add(new BasicNameValuePair("FUNCTIONTYPE", "1"));
        formparams.add(new BasicNameValuePair("ORDERAMOUNT", "1"));
        StringBuilder builder = new StringBuilder();
        builder.append("MERCHANTID=").append(MERCHANTID);
        builder.append("&ORDERSEQ=").append(orderid);
        builder.append("&ORDERREQTRANSEQ=").append("seq" + orderid);
        builder.append("&TELEPHONE=").append(PHONENUM);
        builder.append("&KEY=B2861E6B4F51839C57DE429898DA90A2A66FDA16F1EF5A3C");
        System.out.println(builder.toString());
        formparams.add(new BasicNameValuePair("MAC", CryptTool.md5Digest(builder.toString())));

        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams);

        System.out.println(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("https://webpaywg.bestpay.com.cn/verifyCode.do");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        String body = IOUtils.toString(httpResponse.getEntity().getContent());
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + ":" + body);
    }

    @Test
    public void test(){
        String body = "ORDERSEQ=te20150623233055216IFx4401&ORDERREQTRANSEQ=seq2te20150623233055216IFx4401&UPTRANSEQ=2015062300000185643498&TRANDATE=20150623&ORDERAMOUNT=1&RETNCODE=0000&RETNINFO=0000&BANKACCID=18026375802&MAC=2DAEAC4B762D57A1CD7E4ED7DA6A356B";
        Map<String, String> map = new HashMap<String, String>();
        String[] strs = body.split("&");
        for(String str : strs){
            String[] temp = str.split("=");
            map.put(temp[0], temp[1]);
        }
        String UPTRANSEQ = map.get("UPTRANSEQ");
    }
}
