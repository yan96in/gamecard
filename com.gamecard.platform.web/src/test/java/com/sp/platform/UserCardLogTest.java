package com.sp.platform;

import com.sp.platform.entity.Card;
import com.sp.platform.service.CardService;
import com.sp.platform.service.UserCardLogSerivce;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-19
 * Time: 下午11:34
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserCardLogTest {
    @Autowired
    UserCardLogSerivce userCardLogSerivce;

    @Test
    public void testCardCount(){
        Integer count = userCardLogSerivce.getTodayCardCount("13552922122");
        System.out.println(count);
    }
}
