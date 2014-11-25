package com.sp.platform;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-25
 * Time: 上午7:12
 * To change this template use File | Settings | File Templates.
 */
public class CommonTest {
    @Test
    public void timeFormatTest(){
        String endtime = "2011-04-05 01:02:39";
        endtime = endtime.substring(0, 4) + endtime.substring(5, 7) + endtime.substring(8, 10) + endtime.substring(11, 13) + endtime.substring(14, 16) + endtime.substring(17, 19);
        System.out.println(endtime);
    }
}
