package com.sp.platform.cache;

import com.sp.platform.entity.BlackCode;
import com.sp.platform.service.BlackCodeService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanglei on 15/6/28.
 */
@Service
public class BlackCache extends AbstractBaseTimer {
    private static Map<String, String> cache = new HashMap<String, String>();

    @Autowired
    private BlackCodeService blackCodeService;

    @Override
    public void init() {

    }

    @Override
    public void update() {
        List<BlackCode> list = blackCodeService.getAll();
        for(BlackCode blackCode : list){
            cache.put(blackCode.getCode(), blackCode.getCode());
        }
        LogEnum.TEMP.info("缓存中共存放{}条BlackCode信息", cache.size());
    }

    public static boolean isBlack(String key){
        if(StringUtils.isNotBlank(cache.get(key))){
            return true;
        }
        return false;
    }
}
