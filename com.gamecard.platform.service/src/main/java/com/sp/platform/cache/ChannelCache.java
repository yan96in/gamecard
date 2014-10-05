package com.sp.platform.cache;

import com.sp.platform.common.Constants;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: yangl
 * Date: 13-5-25 下午10:52
 */
@Service
public class ChannelCache extends AbstractBaseTimer {
    /**
     * 被叫号码：通道实体类
     */
    private static Map<String, Paychannel> cache = new ConcurrentHashMap<String, Paychannel>();
    private static Map<Integer, Paychannel> cache2 = new ConcurrentHashMap<Integer, Paychannel>();

    @Autowired
    private PaychannelService paychannelService;

    @Override
    public void init() {
    }

    @Override
    public void update() {
        Map<String, Paychannel> temp;
        Map<Integer, Paychannel> temp2;
        List<Paychannel> list = paychannelService.getAll();
        if (list != null && list.size() > 0) {
            temp = new HashMap<String, Paychannel>();
            temp2 = new HashMap<Integer, Paychannel>();
            String[] sms;
            String key;
            for (Paychannel bean : list) {
                temp.put(bean.getSpnum() + Constants.split_str + bean.getMsg(), bean);
                temp2.put(bean.getId(), bean);
            }
            cache.clear();
            cache.putAll(temp);
            cache2.clear();
            cache2.putAll(temp2);
        }
        LogEnum.TEMP.info("缓存中共存放{}条通道信息", cache.size());
    }

    /**
     * IVR
     * 根据被叫号码获取信息
     *
     * @param key
     * @return
     */
    public static Paychannel get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return cache.get(key);
    }
    public static Paychannel get(Integer key) {
        if (key == null) {
            return null;
        }
        return cache2.get(key);
    }

    public static int getChannelId(String key) {
        Paychannel paychannel = cache.get(key);
        if(paychannel == null){
            return 0;
        }
        return paychannel.getId();
    }
}
