package com.sp.platform;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * User: yangl
 * Date: 13-6-15 下午8:48
 */
public class StressTest {
    String url1 = "http://218.206.72.142:8080/xqsp/receiveivr/4/res.ivr?caller=1355292";
    String url2 = "&called=125901234&btime=2013-06-15%2021:00:01&etime=2013-06-15%2021:00:51";
    int i = 2000;

    @Test
    public void test() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(10);
        for (; i < 2010; i++) {
            httpTest(i, countDownLatch, countDownLatch2);
        }
        countDownLatch.countDown();
        countDownLatch2.await();
        System.out.println("共执行时间：  " + stopWatch.getTime());

        test2();
    }

    public void test2() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(10);
        for (; i < 2020; i++) {
            httpTest(i, countDownLatch, countDownLatch2);
        }
        countDownLatch.countDown();
        countDownLatch2.await();
        System.out.println("共执行时间：  " + stopWatch.getTime());
    }

    public void httpTest(final int caller, final CountDownLatch countDownLatch, final CountDownLatch countDownLatch2) {
        new Thread() {
            public void run() {
                try {
                    countDownLatch.await();
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url1 + caller + url2);
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    stopWatch.stop();
                    System.out.println(response.getStatusLine() + "   执行时间：  " + stopWatch.getTime());
                    System.out.println(IOUtils.toString(entity.getContent()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch2.countDown();
            }
        }.start();
    }

    @Test
    public void bufa() throws Exception {
        File file = new File("C:\\Users\\mopdgg\\Desktop\\sp-platform-sp.log");

        List<String> list = IOUtils.readLines(new FileInputStream(file));
        for(String body : list){
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(body);
            client.execute(get);
            Thread.sleep(100);
        }
    }
}
