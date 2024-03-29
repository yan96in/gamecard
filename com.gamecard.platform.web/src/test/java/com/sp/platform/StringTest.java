package com.sp.platform;

import com.sp.platform.util.XDEncodeHelper;
import com.sp.platform.constants.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: yangl
 * Date: 13-7-2 下午9:51
 */
public class StringTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String body = "北京市、上海市 、重庆市、河北省 、云南省、辽宁省、黑龙江省、山东省、新疆、江苏省、浙江省、江西省、湖北省、广西、甘肃省、山西省、内蒙古、陕西省、福建省、贵州省、广东省、青海省、四川省、宁夏";
        System.out.println(StringUtils.indexOf(body, "浙江"));
    }

    @Test
    public void testBody() throws UnsupportedEncodingException {

        String body = "%c4%fa%d2%d1%be%ad%b7%a2%cb%cd%c1%cb%d7%e3%b9%bb%b5%c4%cc%f5%ca%fd%a3%ac%c7%eb%bb%d8%b8%b48%a3%ac%bd%ab%d4%da1%b7%d6%d6%d3%d2%d4%c4%da%b8%f8%c4%fa%b7%a2%cb%cd%d3%ce%cf%b7%bf%a8%c3%dc%2c%bb%d8%b8%b4%d6%bb%ca%d5%c8%a10.1%d4%aa%b6%cc%d0%c5%cf%a2%b7%d1%d3%c3%5b%b6%df%c0%b2%cd%f8%5d";
        System.out.println(URLDecoder.decode(body, "GBK"));
        System.out.println(Constants.getChannelId("Yxx82".toUpperCase()));

    }

    @Test
    public void test1() throws Exception {
        File file = new File("e://error.txt");
        List<String> list = FileUtils.readLines(file);
        int line = 0;
        List<String> bill = new ArrayList<String>(2000);
        for (String str : list) {

            int i = StringUtils.lastIndexOf(str, "receivesms");

            if (str.indexOf("ZSYL") == -1 && str.indexOf("FBBD") == -1) {
                continue;
            }

            line++;

            bill.add(str.substring(StringUtils.lastIndexOf(str, "receivesms"), str.length() - 1));
            System.out.println(str.substring(StringUtils.lastIndexOf(str, "receivesms"), str.length() - 1));
//
//            HttpClient client = new DefaultHttpClient();
//            HttpGet get = new HttpGet("http://localhost/" + str.substring(StringUtils.lastIndexOf(str, "receivesms"), str.length() - 1));
//            HttpResponse response = client.execute(get);
//            System.out.println(response.getStatusLine().getStatusCode());
//            Thread.sleep(200);
        }
        System.out.println(line);
        File file2 = new File("e://bill.txt");
        FileUtils.writeLines(file2, bill);
    }

    @Test
    public void http() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://pc.rsbwl.com/CBS/jfpc/zhjw_cuc.jsp?uid=13112341234&pid=1");
        HttpResponse response = client.execute(get);
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(IOUtils.toString(response.getEntity().getContent()));
    }

    @Test
    public void bill() throws Exception {
        File file = new File("e://bill.txt");
        List<String> list = FileUtils.readLines(file);
        int line = 0;
        for (String str : list) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://218.206.72.142:8080/spff/receivesms/4/" + str.substring(str.indexOf("res.sms")));
                HttpResponse response = client.execute(get);
                System.out.println(response.getStatusLine().getStatusCode());
                Thread.sleep(10);
                client.getConnectionManager().shutdown();
            } catch (Exception e) {
            }
        }
        System.out.println(line);
    }

    @Test
    public void test2() throws Exception {
        XDEncodeHelper xdEncodeHelper = new XDEncodeHelper("tch5VEeZSAJ2VU4lUoqaYaaP");
        File file = new File("/Users/yanglei/card.txt");
        List<String> list = FileUtils.readLines(file);
        int line = 0;
        Set<String> set = new HashSet<String>();
        Thread.sleep(5000);

        for (String str : list) {
            line++;
            String[] strs = str.split(",");
            System.out.println(xdEncodeHelper.XDDecode(strs[0], true) + "," + xdEncodeHelper.XDDecode(strs[1], true));
        }


        System.out.println(line);
        System.out.println(set.size());
    }

    @Test
    public void testAppName() throws UnsupportedEncodingException {
        String appname = "%E6%B8%B8%E6%88%8F%E9%A3%8E%E6%9A%B4";
        System.out.println(URLDecoder.decode(appname, "utf-8"));

        StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz1234567890");
        StringBuffer sb = new StringBuffer("");
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < 6; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        System.out.println(sb.toString());
    }

    @Test
    public void test() throws Exception {
        List<String> list = IOUtils.readLines(new FileInputStream(new File("/Users/yanglei/black.txt")));
        List<String> l = new ArrayList<String>();
        for (String str : list) {
            l.add("insert into black_code(code) values('" + str + "');");
        }
        FileUtils.writeLines(new File("/Users/yanglei/black2.txt"), l);
    }

    @Test
    public void testDivide(){
        BigDecimal fee = new BigDecimal(1);
        System.out.println(fee.divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN));
    }
}
