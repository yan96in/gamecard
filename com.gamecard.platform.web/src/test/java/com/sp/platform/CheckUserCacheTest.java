package com.sp.platform;

import com.sp.platform.web.cache.CheckUserCache;
import org.junit.Test;

/**
 * Created by yanglei on 15/4/1.
 */
public class CheckUserCacheTest {
    @Test
    public void testUser(){
        for(int i = 0; i< 5; i++) {
            System.out.println(CheckUserCache.checkUser("13552922122"));
        }
    }
}
