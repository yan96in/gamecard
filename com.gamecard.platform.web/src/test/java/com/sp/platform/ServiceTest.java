package com.sp.platform;

import com.sp.platform.service.IvrChannelService;
import com.sp.platform.task.CalloutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: yangl
 * Date: 13-5-27 下午10:13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ServiceTest {
    @Autowired
    private IvrChannelService service;

    @Test
    public void get() {
        System.out.println(service.get(27));
    }
}
