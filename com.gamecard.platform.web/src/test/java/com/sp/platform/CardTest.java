package com.sp.platform;

import com.sp.platform.entity.Card;
import com.sp.platform.service.CardService;
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
public class CardTest {
    @Autowired
    CardService cardService;

    @Test
    public void testCardGet(){
        Card card = cardService.getDetail(1);
        System.out.println(card);
    }
}
