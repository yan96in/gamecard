package com.sp.platform.web.cache;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yanglei on 15/4/1.
 */
public class CheckUserCache {
    private static final Map<String, Integer> userCache = new ConcurrentHashMap<String, Integer>();
    private static final Map<String, Integer> ipCache = new ConcurrentHashMap<String, Integer>();
    private static String date = "";
    private static long lastTime = 0;
    private static final Integer MAX_COUNT = 20;
    private static final Integer MAX_COUNT_IP = 5;

    public static boolean checkUser(String mobile) {
        if (System.currentTimeMillis() - lastTime > 60000) {
            DateTime dateTime = new DateTime();
            String today = dateTime.toString("yyyyMMdd");
            if (!StringUtils.equals(today, date)) {
                userCache.clear();
                date = today;
                lastTime = System.currentTimeMillis();
            }
        }
        Integer count = userCache.get(mobile);
        if (count == null) {
            userCache.put(mobile, 1);
            return true;
        }
        if (count < MAX_COUNT) {
            userCache.put(mobile, count + 1);
            return true;
        }
        return false;
    }

    public static int checkIp(String ip) {
        if (System.currentTimeMillis() - lastTime > 60000) {
            DateTime dateTime = new DateTime();
            String today = dateTime.toString("yyyyMMdd");
            if (!StringUtils.equals(today, date)) {
                ipCache.clear();
                date = today;
                lastTime = System.currentTimeMillis();
            }
        }
        Integer count = ipCache.get(ip);
        if (count == null) {
            ipCache.put(ip, 1);
            return 1;
        }

        return count;
    }

    public static void addIp(String ip){
        Integer count = ipCache.get(ip);
        if (count == null) {
            ipCache.put(ip, 1);
            return;
        }
        ipCache.put(ip, count + 1);
    }
}
