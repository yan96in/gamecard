package com.sp.platform;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by yanglei on 15/2/14.
 */
public class DateTimeTest {
    @Test
    public void minuteTest(){
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(-30);
        System.out.println(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }
}
